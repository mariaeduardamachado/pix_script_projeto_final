package br.edu.ifgoiano.pixscript.tree;

import br.edu.ifgoiano.pixscript.ast.AstNode;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeImageGenerator {
    private static final int BOX_W = 150;
    private static final int BOX_H = 46;
    private static final int H_GAP = 35;
    private static final int V_GAP = 82;
    private static final int MARGIN = 40;

    public void saveDot(AstNode root, Path dotFile) throws IOException {
        Files.createDirectories(dotFile.getParent());
        Files.writeString(dotFile, root.toDot(), StandardCharsets.UTF_8);
    }

    public void savePng(AstNode root, Path pngFile) throws IOException {
        Files.createDirectories(pngFile.getParent());
        Layout layout = layout(root);
        int width = Math.max(900, layout.width + MARGIN * 2);
        int height = Math.max(500, layout.height + MARGIN * 2);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        drawEdges(g, root, layout.positions);
        drawNodes(g, root, layout.positions);
        g.dispose();
        ImageIO.write(image, "png", pngFile.toFile());
    }

    private Layout layout(AstNode root) {
        Map<AstNode, Point> positions = new HashMap<>();
        int[] nextX = {MARGIN};
        int maxDepth = assign(root, 0, nextX, positions);
        return new Layout(positions, nextX[0] + BOX_W, (maxDepth + 1) * (BOX_H + V_GAP));
    }

    private int assign(AstNode node, int depth, int[] nextX, Map<AstNode, Point> positions) {
        List<AstNode> children = node.getChildren();
        int maxDepth = depth;
        if (children.isEmpty()) {
            positions.put(node, new Point(nextX[0], MARGIN + depth * (BOX_H + V_GAP)));
            nextX[0] += BOX_W + H_GAP;
        } else {
            List<Integer> childCenters = new ArrayList<>();
            for (AstNode child : children) {
                maxDepth = Math.max(maxDepth, assign(child, depth + 1, nextX, positions));
                Point p = positions.get(child);
                childCenters.add(p.x + BOX_W / 2);
            }
            int min = childCenters.stream().mapToInt(Integer::intValue).min().orElse(nextX[0]);
            int max = childCenters.stream().mapToInt(Integer::intValue).max().orElse(nextX[0]);
            int x = (min + max) / 2 - BOX_W / 2;
            positions.put(node, new Point(x, MARGIN + depth * (BOX_H + V_GAP)));
        }
        return maxDepth;
    }

    private void drawEdges(Graphics2D g, AstNode node, Map<AstNode, Point> positions) {
        Point parent = positions.get(node);
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(1.4f));
        for (AstNode child : node.getChildren()) {
            Point childPoint = positions.get(child);
            g.drawLine(parent.x + BOX_W / 2, parent.y + BOX_H, childPoint.x + BOX_W / 2, childPoint.y);
            drawEdges(g, child, positions);
        }
    }

    private void drawNodes(Graphics2D g, AstNode node, Map<AstNode, Point> positions) {
        Point p = positions.get(node);
        g.setColor(new Color(245, 248, 255));
        g.fillRoundRect(p.x, p.y, BOX_W, BOX_H, 12, 12);
        g.setColor(new Color(50, 70, 100));
        g.drawRoundRect(p.x, p.y, BOX_W, BOX_H, 12, 12);
        drawCenteredText(g, node.label(), p.x, p.y, BOX_W, BOX_H);
        for (AstNode child : node.getChildren()) {
            drawNodes(g, child, positions);
        }
    }

    private void drawCenteredText(Graphics2D g, String text, int x, int y, int w, int h) {
        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        String[] lines = text.split("\\\\n|\\n");
        int totalHeight = lines.length * fm.getHeight();
        int startY = y + (h - totalHeight) / 2 + fm.getAscent();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int tx = x + (w - fm.stringWidth(line)) / 2;
            g.drawString(line, tx, startY + i * fm.getHeight());
        }
    }

    private record Point(int x, int y) {}
    private record Layout(Map<AstNode, Point> positions, int width, int height) {}
}
