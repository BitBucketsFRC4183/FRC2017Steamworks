import cv2

class Rope:
    """
    An OpenCV pipeline created to find faces
    """
    
    def __init__(self):
        """initializes all values to presets or None if need to be set
        """
     

    def process(self, source0):
        """
        Runs the pipeline and sets all outputs to new values.
        """
        
        # Draw reticle for rope guide
        s = source0.shape
        pt1 = (int(s[0]/4),0)
        pt2 = (int(s[0]/4),s[1])
        color = (0,255,0)
        thickness = 2
        cv2.line(source0,pt1,pt2,color,thickness)
        pt1 = (int(3*s[0]/4),0)
        pt2 = (int(3*s[0]/4),s[1])
        cv2.line(source0,pt1,pt2,color,thickness)
        