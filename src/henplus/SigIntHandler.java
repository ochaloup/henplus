/*
 * This is free software, licensed under the Gnu Public License (GPL)
 * get a copy from <http://www.gnu.org/licenses/gpl.html>
 * $Id: SigIntHandler.java,v 1.4 2002-06-10 17:38:11 hzeller Exp $ 
 * author: Henner Zeller <H.Zeller@acm.org>
 */
package henplus;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Signal handler, that reacts on CTRL-C.
 */
public class SigIntHandler implements SignalHandler {
    private boolean once;
    private static SigIntHandler instance = null;
    private Interruptable toInterrupt = null;

    public static SigIntHandler install() {
	Signal interruptSignal = new Signal("INT"); // Interrupt: Ctrl-C
        instance = new SigIntHandler();
	// don't care about the original handler.
	Signal.handle(interruptSignal, instance);
	return instance;
    }
    
    public static SigIntHandler getInstance() {
	return instance;
    }

    public SigIntHandler() {
	once = false;
    }
    
    public void registerInterrupt(Interruptable t) {
	toInterrupt = t;
    }
    
    public void reset() {
	once = false;
	toInterrupt = null;
    }

    public void handle(Signal sig) {
	if (once) {
	    // got the interrupt twice. Just exit.
	    return;
	}
	once = true;
	System.err.println("[Ctrl-C ; interrupted]");
	if (toInterrupt != null) {
	    // this doesn't work, since the JDBC driver is not in a 'wait()'
	    //System.err.println("try to interrupt: " + toInterrupt);
	    toInterrupt.interrupt();
	    toInterrupt = null;
	}
	else {
	    System.exit(1);
	}
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
