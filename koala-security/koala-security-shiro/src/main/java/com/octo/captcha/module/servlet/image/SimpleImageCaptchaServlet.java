package com.octo.captcha.module.servlet.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Minimal compatibility replacement for the old JCaptcha servlet API used by
 * this project.
 */
public class SimpleImageCaptchaServlet extends HttpServlet {

    private static final long serialVersionUID = 8565075231455448147L;

    private static final String CAPTCHA_SESSION_KEY = SimpleImageCaptchaServlet.class.getName() + ".CODE";

    private static final char[] CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();

    private static final SecureRandom RANDOM = new SecureRandom();

    public static boolean validateResponse(HttpServletRequest request, String response) {
        if (request == null || response == null) {
            return false;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Object expected = session.getAttribute(CAPTCHA_SESSION_KEY);
        session.removeAttribute(CAPTCHA_SESSION_KEY);
        return expected != null && response.trim().equalsIgnoreCase(expected.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = nextCode(4);
        request.getSession(true).setAttribute(CAPTCHA_SESSION_KEY, code);

        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0L);

        ImageIO.write(render(code), "jpg", response.getOutputStream());
    }

    private static String nextCode(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(CHARS[RANDOM.nextInt(CHARS.length)]);
        }
        return result.toString();
    }

    private static BufferedImage render(String code) {
        int width = 96;
        int height = 36;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(new Color(245, 247, 250));
            graphics.fillRect(0, 0, width, height);

            for (int i = 0; i < 12; i++) {
                graphics.setColor(randomSoftColor());
                int x1 = RANDOM.nextInt(width);
                int y1 = RANDOM.nextInt(height);
                int x2 = RANDOM.nextInt(width);
                int y2 = RANDOM.nextInt(height);
                graphics.drawLine(x1, y1, x2, y2);
            }

            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
            for (int i = 0; i < code.length(); i++) {
                graphics.setColor(randomTextColor());
                graphics.drawString(String.valueOf(code.charAt(i)), 14 + i * 19, 26 + RANDOM.nextInt(5) - 2);
            }
            return image;
        } finally {
            graphics.dispose();
        }
    }

    private static Color randomSoftColor() {
        return new Color(150 + RANDOM.nextInt(80), 150 + RANDOM.nextInt(80), 150 + RANDOM.nextInt(80));
    }

    private static Color randomTextColor() {
        return new Color(20 + RANDOM.nextInt(80), 20 + RANDOM.nextInt(80), 20 + RANDOM.nextInt(80));
    }
}
