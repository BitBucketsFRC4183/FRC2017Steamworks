
from threading import Lock
from threading import Thread
from threading import Condition

class BucketServer2:
    def __init__(self,name, sender):
        print("Creating BucketServer for " + name)
        self._lock = Lock()
        self._condition = Condition()
        self.name = name

        self.sender = sender
        
        # initialize the variable used to indicate if the thread should
        # be stopped
        self._stop = False
        self.stopped = True

        print("BucketServer created for " + self.name)
        
    def start(self):
        print("STARTING BucketServer for " + self.name)
        t = Thread(target=self.update, args=())
        t.daemon = True
        t.start()
        return self

    def update(self):
        print("BucketServer for " + self.name + " RUNNING")
        # keep looping infinitely until the thread is stopped
        self.stopped = False
        
        while True:
            # if the thread indicator variable is set, stop the thread
            if (self._stop == True):
                self._stop = False
                self.stopped = True
                return

            self.sender.do()
                
        print("BucketProcessor for " + self.name + " STOPPING")

    def stop(self):
        # indicate that the thread should be stopped
        self._stop = True
        self._condition.acquire()
        self._condition.notifyAll()
        self._condition.release()

    def isStopped(self):
        return self.stopped
		
