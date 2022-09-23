package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

import java.io.*;
import java.net.URL;

public class Compiler
{
    static
    {
        URL tmp = Log4JUtils.instance().findLoggerConfigFile();
        DOMConfigurator.configure(tmp);
        Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
        log = Logger.getLogger(Compiler.class);
    }

    private static final Logger log;

    public static void main(String[] args) throws Exception
    {
        if(args.length < 2)
        {
            log.error("Potrebno je proslediti 2 argumenta programu 1:fajl za prevodjenje 2:lokacija/ime za objektni fajl");
            return;
        }
        Reader br = null;
        try
        {
            File sourceCode = new File(args[0]);
            if(!sourceCode.exists())
            {
                log.error(sourceCode.getName() + " ne postoji.");
                System.err.println(sourceCode.getName() + " ne postoji.");
                return;
            }
            log.info("Compiling source file: " + sourceCode.getAbsolutePath());
            System.out.println("Compiling source file: " + sourceCode.getAbsolutePath());

            br = new BufferedReader(new FileReader(sourceCode));
            Yylex lexer = new Yylex(br);

            MJParser p = new MJParser(lexer);
            Symbol s = p.parse();  //pocetak parsiranja
            if(p.errorDetected)
            {
                log.error("Doslo je do greske prilikom parsiranja.");
                System.err.println("Doslo je do greske prilikom parsiranja.");
                return;
            }
            log.info("Parsiranje uspesno zavrseno.");
            System.out.println("Parsiranje uspesno zavrseno.");

            Program prog = (Program) (s.value);
            log.info(prog.toString(""));
            SemanticAnalyzer analyzer = new SemanticAnalyzer();
            log.info("==================SEMANTICKA OBRADA====================");
            prog.traverseBottomUp(analyzer);
            if(analyzer.isErrorDetected())
            {
                log.error("Doslo je do greske kod semanticke analize.");
                System.err.println("Doslo je do greske kod semanticke analize.");
                return;
            }
            log.info("Semanticka analiza uspesno zavrsena.");
            System.out.println("Semanticka analiza uspesno zavrsena.");
            tsdump();
            //za debugging
            //customPrint();
            CodeGenerator generator = new CodeGenerator();
            generator.setDataSize(analyzer.getBrojGlobalnihPromenljivi());
            generator.setVft(analyzer.getBrojGlobalnihPromenljivi());//virtual function table pocinje posle svih globalnih promenljivi
            prog.traverseBottomUp(generator);
            Code.mainPc = generator.getStartProgramAddress();
            Code.dataSize = generator.getDataSize();
            if(generator.generate(args[1]))
            {
                log.info("Objektni fajl za " + args[0] + " uspesno generisan kao " + args[1]);
                System.out.println("Objektni fajl za " + args[0] + " uspesno generisan kao " + args[1]);
            }
            else
            {
                log.error("Greska prilikom pisanja objektnog fajla.");
                System.err.println("Greska prilikom pisanja objektnog fajla.");
            }
        }
        finally
        {
            if(br != null)
                try
                {
                    br.close();
                }
                catch(IOException e1)
                {
                    log.error(e1.getMessage(), e1);
                }
        }

    }

    private static void tsdump()
    {
        DumpSymbolTableVisitor tmp = new DumpSymbolTableVisitor();
        Tab.dump(tmp);
        log.info("=====================SYMBOL TABLE DUMP=========================" + System.lineSeparator() + tmp.getOutput());
    }

    private static void customPrint()
    {

        System.out.println("\n\n");
        for(ClassObj obj : ClassObj.getClasses())
        {

            System.out.println(obj.getName());
            for(Obj fields : obj.getFields())
                System.out.println("\t" + fields.toString() + " " + fields.getName() + " " + fields.getFpPos() + " " + fields.getType());
            for(MethodObj methodObj : obj.getMethods())
            {
                System.out.println("\t" + methodObj.getName() + " " + methodObj.id);
                for(Obj locals : methodObj.getMethod().getLocalSymbols())
                    System.out.println("\t\t" + locals.toString() + " " + locals.getName() + " " + locals.getFpPos() + " " + locals.getType());
            }
        }
        System.out.println("Sve metode:");
        for(MethodObj methodObj : MethodObj.allMethods)
        {
            System.out.println("\t" + methodObj.getName() + " " + methodObj.id);
            for(Obj locals : methodObj.getMethod().getLocalSymbols())
                System.out.println("\t\t" + locals.toString() + " " + locals.getName() + " " + locals.getFpPos() + " " + locals.getType());
        }
    }
}
