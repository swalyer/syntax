import java.util.ArrayList;
import java.util.List;

class TreeNode {
    String value;
    List<TreeNode> children;

    public TreeNode(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public String toString() {
        return toStringHelper(0);
    }

    private String toStringHelper(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        sb.append(value).append("\n");
        for (TreeNode child : children) {
            sb.append(child.toStringHelper(indent + 1));
        }
        return sb.toString();
    }
}

public class Parser {
    private final Tokenizer tokenizer;
    private String currentToken;

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.currentToken = tokenizer.hasNext() ? tokenizer.next() : null;
    }

    private void consume(String expected) {
        if (expected.equals(currentToken)) {
            currentToken = tokenizer.hasNext() ? tokenizer.next() : null;
        } else {
            throw new IllegalStateException("Expected " + expected + " but found " + currentToken);
        }
    }

    public TreeNode parseStatement() {
        if (currentToken.matches("\\w+")) {
            String identifier = currentToken;
            consume(currentToken);
            consume(":=");
            TreeNode expression = parseExpression();
            TreeNode assignment = new TreeNode("Assignment");
            assignment.addChild(new TreeNode("Identifier: " + identifier));
            assignment.addChild(expression);
            return assignment;
        } else {
            throw new IllegalStateException("Unexpected token: " + currentToken);
        }
    }

    public TreeNode parseExpression() {
        TreeNode node = parseTerm();
        while (currentToken != null && (currentToken.equals("+") || currentToken.equals("-"))) {
            String operator = currentToken;
            consume(currentToken);
            TreeNode right = parseTerm();
            TreeNode newNode = new TreeNode(operator);
            newNode.addChild(node);
            newNode.addChild(right);
            node = newNode;
        }
        return node;
    }

    public TreeNode parseTerm() {
        TreeNode node = parseFactor();
        while (currentToken != null && (currentToken.equals("*") || currentToken.equals("/"))) {
            String operator = currentToken;
            consume(currentToken);
            TreeNode right = parseFactor();
            TreeNode newNode = new TreeNode(operator);
            newNode.addChild(node);
            newNode.addChild(right);
            node = newNode;
        }
        return node;
    }

    public TreeNode parseFactor() {
        if (currentToken.matches("\\w+")) {
            TreeNode node = new TreeNode("Identifier: " + currentToken);
            consume(currentToken);
            return node;
        } else if (currentToken.matches("\\'.\\'")) {
            TreeNode node = new TreeNode("Character: " + currentToken);
            consume(currentToken);
            return node;
        } else if (currentToken.equals("(")) {
            consume("(");
            TreeNode node = parseExpression();
            consume(")");
            return node;
        } else {
            throw new IllegalStateException("Unexpected token: " + currentToken);
        }
    }

    public static void main(String[] args) {
        String input = "x := 5 + y * (z - 3); y := 'a';";
        Tokenizer tokenizer = new Tokenizer(input);
        Parser parser = new Parser(tokenizer);
        List<TreeNode> statements = new ArrayList<>();
        while (tokenizer.hasNext()) {
            statements.add(parser.parseStatement());
            if (tokenizer.hasNext()) {
                parser.consume(";");
            }
        }
        for (TreeNode statement : statements) {
            System.out.println(statement);
        }
    }
}
