package org.usfirst.frc.team4183.utils;
import java.util.List;
import java.util.Vector;

public class PurePursuit {
	
	// All distances in inches
	// All angles in degrees
	
	private final Double[][] _waypts;
	
	private List<Double[]> _path = new Vector<Double[]>();
	private List<Double> _pathLen = new Vector<Double>();
	
	private final double _lookAhead;
	private final double _maxSubsegLen = 3.0;
	private final double _closestSearchFactor = 10.0;
	
	private int _lastWayptIndx = 0;
	private int _closestIndx = 0;
	
	// lookAhead is lookahead distance
	// waypts are the path waypoints, one point per row
	PurePursuit( double lookAhead, double[][] waypts) {
		
		_lookAhead = lookAhead;
		_waypts = new Double[waypts.length][2];
		for( int i = 0 ; i < waypts.length ; i++) {
			_waypts[i][0] = waypts[i][0];
			_waypts[i][1] = waypts[i][1];
		}

		// Create _path, _pathLen
		double pathLenToSeg = 0.0;
		for( int i = 0 ; i <= waypts.length - 2 ; i++) {
			
			// segment is vector from waypt[i] to waypt[i+1]
			Double[] seg = subtract( _waypts[i+1], _waypts[i]);
			
			// Divide each segment into subsegments, 
			// each subsegment no longer than maxSubsegLen
			int nSubseg = (int)Math.ceil( norm(seg)/_maxSubsegLen);
			double subsegLen = norm(seg)/nSubseg;
			
			// Extend last segment past last waypoint by lookAhead
			int extraSubseg = 0;
			if( i == waypts.length - 2 ) {
				extraSubseg = (int)Math.ceil(lookAhead/subsegLen) + 1;
			}
			
			// Calculate path points & lengths, add to Lists
			for( int j = 0 ; j < nSubseg + extraSubseg ; j++) {
				Double[] v = scalarMult( seg, (double)j/nSubseg);
				_path.add( sum( v, _waypts[i]));
				_pathLen.add( pathLenToSeg + norm(v) );
				
				if( j == nSubseg)
					_lastWayptIndx = _path.size() - 1;
			}
			pathLenToSeg += norm(seg);
		}		
	}

	// Returned by get()
	public class Info {
		public double curve;
		public double distToEnd;
	}
	
	// {(px, py), yaw_deg} gives current robot pose in global coords
	// Returns curvature in deg/inch
	// (multiply curvature by forward velocity inch/sec to get yaw rate deg/sec)
	// Also returns distance to end of path.
	public Info get( double px, double py, double yaw_deg) {
		
		Double[] p = new Double[2]; p[0] = px; p[1] = py;
		
		Double[] targetG = findGoal(p);		
		Double[] targetL = toLocal( p, yaw_deg, targetG);
		double d = norm(targetL);
		double y = targetL[1];
		
		Info rslt = new Info();
		rslt.curve = (180.0/Math.PI)*2.0*y/(d*d);
		rslt.distToEnd = _pathLen.get( _lastWayptIndx) - _pathLen.get( _closestIndx);
		return rslt;
	}
	
		
	// p is current robot position in global coords
	// Returns goal path point
	private Double[] findGoal( Double[] p) {
		
		findClosest(p);
		
		for( int i = _closestIndx ; i < _path.size() ; i++) {
			double d = norm( subtract( _path.get(i), p));
			if( d >= _lookAhead) 
				return _path.get(i);			
		}
		return _path.get( _path.size()-1);
	}
	
	
	// p is current robot position in global coords
	// Sets member _closestIndx to index of path point closest to p
	private void findClosest( Double[] p) {
		
		double dmin = Double.MAX_VALUE;
		int imin = -1;
		for( int i = _closestIndx ; i < _path.size() ; i++) {
			double d = norm( subtract(p, _path.get(i)));
			if( d <= dmin) {
				dmin = d;
				imin = i;
			}
			// Truncate the search at some arbitrary distance up the path
			// (silly to search all the way to end of path each time)
			if( _pathLen.get(i) - _pathLen.get(_closestIndx) > _closestSearchFactor*_lookAhead )
				break;
		}
		_closestIndx = imin;
	}

	// {p, yaw_deg} is current robot pose in global coords
	// pg is the point in global coords to be converted to robot local coords
	// Returns pg in robot local coords
	private Double[] toLocal( Double[] p, double yaw_deg, Double[] pg) {

		Double[] rslt = new Double[2];
		Double[] v = subtract( pg, p);
		
		double c = Math.cos(Math.PI*yaw_deg/180.0);
		double s = Math.sin(Math.PI*yaw_deg/180.0);		
		rslt[0] = c*v[0] + s*v[1];
		rslt[1] = -s*v[0] + c*v[1];
		
		return rslt;
	}
	
	// Returns |v|
	private double norm( Double[] v) {
		return Math.sqrt(v[0]*v[0] + v[1]*v[1]);
	}
	
	// Returns v*t
	private Double[] scalarMult( Double[] v, double t) {
		Double[] rslt = new Double[2];
		rslt[0] = t*v[0];
		rslt[1] = t*v[1];
		return rslt;
	}

	// Returns v2 + v1
	private Double[] sum( Double[] v2, Double[] v1) {
		Double[] rslt = new Double[2];
		rslt[0] = v2[0] + v1[0];
		rslt[1] = v2[1] + v1[1];
		return rslt;		
	}

	// Returns v2 - v1
	private Double[] subtract( Double[] v2, Double[] v1) {
		Double[] rslt = new Double[2];
		rslt[0] = v2[0] - v1[0];
		rslt[1] = v2[1] - v1[1];
		return rslt;
	}
}

