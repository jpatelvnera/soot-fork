package soot;

import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Sources;
import soot.util.queue.QueueReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
//                "/Users/pjital/repos/main/programs/target/classes",
//                "/Users/pjital/repos/bugs-plugin/target/test-classes",

public class CustomMain {
    public static void main(String[] args) {
        List<String> argsList = new ArrayList<String>(Arrays.asList(args));
        argsList.addAll(Arrays.asList("-w",
                "-allow-phantom-refs",
                "-cp",
                "/Users/pjital/repos/bugs-plugin/target/test-classes",
                "-main-class",
                "com.vnera.programs.RunnerClass2", //main-class
                "-process-dir", //argument classes
                "/Users/pjital/repos/bugs-plugin/target/test-classes",
//                "-p",
//                "cg.spark",
//                "verbose:true,propagator:worklist,simple-edges-bidirectional:false,on-fly-cg:true,enabled:true,set-impl:double",
                "-f",
                "J",
                "-p",
                "jap.abc",
                "on"
                ));

        args = argsList.toArray(new String[0]);

        try {
            Main.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CallGraph cg = Scene.v().getCallGraph();
        QueueReader<Edge> listener = cg.listener();
        while (listener.hasNext()) {
            Edge next = listener.next();
            if (!next.getTgt().method().declaringClass.name.startsWith("com.vnera.model."))
                System.out.println("Edge: " + next.getSrc() + "<-------------->" + next.getTgt());
            System.out.println("Context : " + next.getSrc().method().context());
        }

    }

    public void printPossibleCallers(SootMethod target) {
        CallGraph cg = Scene.v().getCallGraph();
        Iterator sources = new Sources(cg.edgesInto(target));
        while (sources.hasNext()) {
            SootMethod src = (SootMethod)sources.next();
            System.out.println(target + " might be called by " + src);
        }
    }
}