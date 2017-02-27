package end2endtest;

import classfiletests.utils.ClassCompiler;
import classfiletests.utils.ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L1Logger;
import utils.staticResults.BeforeAfterContainer;
import utils.staticResults.MSLMap;
import utils.staticResults.MSMap;
import utils.staticResults.ResultsServer;
import utils.staticResults.implementation.Types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Desribe how we want the fake analysis results the be
 */
enum StaticAnalysis {
    allDynamic,         // Var, Cx & Instantiation all return Dynamic on any request
    CxPublic,           // same as allDynamic, except for Cx, which returns public on any request
    VarAndCxPublic      // same as CxPublic, except for Var, which returns public on any request
}

/**
 * Tests to test the impact of different static analysis results.
 */
@RunWith(Parameterized.class)
public class AllFakeAnalysisTests {

    private final String name;
    private final boolean hasIllegalFlow;
    private final StaticAnalysis analysisResult;
    private final String[] involvedVars;
    private Logger logger = L1Logger.getLogger();

    public AllFakeAnalysisTests(String name, boolean hasIllegalFlow,
                                StaticAnalysis analysisResult, String... involvedVars) {

        this.name = name;
        this.hasIllegalFlow = hasIllegalFlow;
        this.analysisResult = analysisResult;
        this.involvedVars = involvedVars;
    }

    /**
     * Testing the same testcases with different static analysis results. For example,
     * we expect an NSU/IllegalFLow Exception when running NSUPolicy in dynamic CX, but if the CX
     * is pulic, we do not expect an NSU / IllegalFlow Exception.
     */
    @Parameterized.Parameters(name = "Name: {0}, {2}")
    public static Iterable<Object[]> generateParameters() {
        return Arrays.asList(
                // should throw a IllegalFlow Exception regardless of the CX
                new Object[] { "AccessFieldsOfObjectsFail", true, StaticAnalysis.allDynamic, new String[] { "java.lang.String_$r6" } },
                new Object[] { "AccessFieldsOfObjectsFail", true, StaticAnalysis.CxPublic, new String[] { "java.lang.String_$r6" } },

                // NSU on local Variables.
                new Object[] { "NSUPolicy", true, StaticAnalysis.allDynamic, new String[] {"int_i0"} },
                new Object[] { "NSUPolicy", false, StaticAnalysis.CxPublic, new String[] {} },

                // NSU on fields.
                new Object[] { "NSUPolicy3", true, StaticAnalysis.allDynamic, new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSUPolicy3", false, StaticAnalysis.CxPublic, new String[] {} },

                // NSU on static fields
                new Object[] { "NSU_FieldAccessStatic", true, StaticAnalysis.allDynamic, new String[] {"int f"} },
                new Object[] { "NSU_FieldAccessStatic", false, StaticAnalysis.CxPublic, new String[] {} },

                // NSU on static fields
                new Object[] { "NSU_FieldAccess", true, StaticAnalysis.allDynamic,  new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess", false, StaticAnalysis.CxPublic, new String[] {} },

                new Object[] { "NSU_FieldAccess2", false, StaticAnalysis.allDynamic,  new String[] {} },    // does not throw an IllFlow Except
                new Object[] { "NSU_FieldAccess2", false, StaticAnalysis.CxPublic, new String[] {} },
                new Object[] { "NSU_FieldAccess2", false, StaticAnalysis.VarAndCxPublic, new String[] {} },

                new Object[] { "NSU_FieldAccess3", true, StaticAnalysis.allDynamic,  new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess3", false, StaticAnalysis.CxPublic, new String[] {} },

                new Object[] { "NSU_FieldAccess4", true, StaticAnalysis.allDynamic,  new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess4", false, StaticAnalysis.CxPublic, new String[] {} },

                new Object[] { "NSU_FieldAccess5", true, StaticAnalysis.allDynamic,  new String[] {"<testclasses.utils.C: boolean f>"} },
                new Object[] { "NSU_FieldAccess5", false, StaticAnalysis.CxPublic, new String[] {} }

                //

       );
    }

    /**
     * Runs each testfile specified above. note that the outputDir can be set to ones liking.
     */
    @Test
    public void test() {
        System.out.println("\n\n\n");
        logger.info("Start of executing testclasses with fake analysis results." + name + "");
        String outputDir = "junit_fakeAnalysis";

        // here, we need to create the appropriate fake Maps to hand over the the ClassCompiler
        MSLMap<BeforeAfterContainer> fakeVarTypingsMap = new MSLMap<>();
        MSMap<Types> fakeCxTypingsMap = new MSMap<>();
        MSMap<Types> fakeInstantiationMap = new MSMap<>();

        Collection<String> allClasses = Collections.singletonList("testclasses." + name);
        switch (analysisResult) {
            case allDynamic:
                ResultsServer.setDynamic(fakeVarTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case CxPublic:
                ResultsServer.setDynamic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            case VarAndCxPublic:
                ResultsServer.setPublic(fakeVarTypingsMap, allClasses);
                ResultsServer.setPublic(fakeCxTypingsMap, allClasses);
                ResultsServer.setDynamic(fakeInstantiationMap, allClasses);
                break;
            default:
                throw new InternalAnalyzerException("Invalid analysis result requested!");
        }



        ClassCompiler.compileWithFakeTyping(name, outputDir, fakeVarTypingsMap, fakeCxTypingsMap, fakeInstantiationMap);
        ClassRunner.testClass(name, outputDir, hasIllegalFlow, involvedVars);

        logger.info("Finished executing testclasses with fake analysis results." + name + "");
    }

    }