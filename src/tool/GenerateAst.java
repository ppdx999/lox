package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class GenerateAst {
    private static final Map<String, Map<String, String>> map;

    static {
        map = new HashMap<>();
        map.put("Expr", new HashMap<>());
        map.put("Stmt", new HashMap<>());

        var exprMap = map.get("Expr");
        exprMap.put("Assign", "Token name, Expr value");
        exprMap.put("Binary", "Expr left, Token operator, Expr right");
        exprMap.put("Call", "Expr callee, Token paren, List<Expr> arguments");
        exprMap.put("Grouping", "Expr expression");
        exprMap.put("Literal", "Object value");
        exprMap.put("Logical", "Expr left, Token operator, Expr right");
        exprMap.put("Unary", "Token operator, Expr right");
        exprMap.put("Variable", "Token name");

        var stmtMap = map.get("Stmt");
        stmtMap.put("Block", "List<Stmt> statements");
        stmtMap.put("Expression", "Expr expression");
        stmtMap.put("Function", "Token name, List<Token> params, List<Stmt> body");
        stmtMap.put("If", "Expr condition, Stmt thenBranch, Stmt elseBranch");
        stmtMap.put("Print", "Expr expression");
        stmtMap.put("Return", "Token keyword, Expr value");
        stmtMap.put("Var", "Token name, Expr initializer");
        stmtMap.put("While", "Expr condition, Stmt body");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        for (var entry : map.entrySet()) {
            defineAst(outputDir, entry.getKey(), entry.getValue());
        }
    }

    private static void defineAst(String outputDir, String baseName, Map<String, String> map)
            throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package jlox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, map);

        for (var entry : map.entrySet()) {
            String className = entry.getKey();
            String fields = entry.getValue();
            defineType(writer, baseName, className, fields);
        }

        writer.println();
        writer.println("  abstract <R> R accept(Visitor<R> visitor);");


        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(PrintWriter writer, String baseName,
            Map<String, String> map) {
        writer.println("  interface Visitor<R> {");

        for (var entry : map.entrySet()) {
            String className = entry.getKey();
            writer.println("    R visit" + className + baseName + "(" + className + " "
                    + baseName.toLowerCase() + ");");
        }

        writer.println("  }");
    }


    private static void defineType(PrintWriter writer, String baseName, String className,
            String fieldList) {
        writer.println("  static class " + className + " extends " + baseName + " {");

        // Constructor.
        writer.println("    " + className + "(" + fieldList + ") {");

        // Paramethers
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // Visitor
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" + className + baseName + "(this);");
        writer.println("    }");


        // Fields
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println("  }");
    }
}
