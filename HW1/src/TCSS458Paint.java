import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.*;
import javax.swing.*;

import java.io.*;
import java.util.*;


public class TCSS458Paint extends JPanel
{
    static int width;
    static int height;
    int imageSize;
    int[] pixels;


    void drawPixel(int x, int y, int r, int g, int b)
    {
        pixels[(height - y - 1) * width * 3 + x * 3] = r;
        pixels[(height - y - 1) * width * 3 + x * 3 + 1] = g;
        pixels[(height - y - 1) * width * 3 + x * 3 + 2] = b;
    }

    void createImage()
    {
        Scanner input = getFile();
        double r = 0;
        double g = 0;
        double b = 0;
        int[][] scanLine = new int[height][2];
        while (input.hasNext())
        {
            for (int i = 0; i < height; i++)
            {
                scanLine[i][0] = Integer.MAX_VALUE;
                scanLine[i][1] = Integer.MIN_VALUE;
            }
            String command = input.next();
            if (command.equals("DIM"))
            {
                width = input.nextInt();
                height = input.nextInt();
                imageSize = width * height;
                pixels = new int[imageSize * 3];
                for (int i = 0; i < pixels.length; i++)
                {
                    pixels[i] = 255;
                }
            }
            else if (command.equals("LINE"))
            {
                double xSub1 = input.nextDouble();
                double ySub1 = input.nextDouble();
                double xSub2 = input.nextDouble();
                double ySub2 = input.nextDouble();
                double x1 = (width - 1) * (xSub1 + 1) / 2;
                double x2 = (width - 1) * (xSub2 + 1) / 2;
                double y1 = (height - 1) * (ySub1 + 1) / 2;
                double y2 = (height - 1) * (ySub2 + 1) / 2;
                makeLine(x1, x2, y1, y2, r, g, b);
            }
            else if (command.equals("RGB"))
            {
                r = input.nextDouble();
                g = input.nextDouble();
                b = input.nextDouble();
            }
            else if (command.equals("TRI"))
            {
                double xSub1 = input.nextDouble();
                double ySub1 = input.nextDouble();
                double xSub2 = input.nextDouble();
                double ySub2 = input.nextDouble();
                double xSub3 = input.nextDouble();
                double ySub3 = input.nextDouble();
                double x1 = ((width - 1) * (xSub1 + 1) / 2);
                double x2 = ((width - 1) * (xSub2 + 1) / 2);
                double x3 = ((width - 1) * (xSub3 + 1) / 2);
                double y1 = ((height - 1) * (ySub1 + 1) / 2);
                double y2 = ((height - 1) * (ySub2 + 1) / 2);
                double y3 = ((height - 1) * (ySub3 + 1) / 2);
                makeTriSide(x1, y1, x2, y2, r, g, b, scanLine);
                makeTriSide(x1, y1, x3, y3, r, g, b, scanLine);
                makeTriSide(x2, y2, x3, y3, r, g, b, scanLine);
                fillTri(scanLine, r, g, b);
            }
        }
    }


    public void paintComponent(Graphics g)
    {
        createImage();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster wr_raster = image.getRaster();
        wr_raster.setPixels(0, 0, width, height, pixels);
        g.drawImage(image, 0, 0, null);
    }

    public static void main(String args[])
    {
        JFrame frame = new JFrame("LINE DEMO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        selectFile();

        JPanel rootPane = new TCSS458Paint();
        getDim(rootPane);
        rootPane.setPreferredSize(new Dimension(width, height));

        frame.getContentPane().add(rootPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    static File selectedFile = null;

    static private void selectFile()
    {
        int approve; //return value from JFileChooser indicates if the user hit cancel

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        approve = chooser.showOpenDialog(null);
        if (approve != JFileChooser.APPROVE_OPTION)
        {
            System.exit(0);
        }
        else
        {
            selectedFile = chooser.getSelectedFile();
        }
    }

    static private Scanner getFile()
    {
        Scanner input = null;
        try
        {
            input = new Scanner(selectedFile);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "There was an error with the file you chose.",
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return input;
    }

    static void getDim(JPanel rootPane)
    {
        Scanner input = getFile();
        String command = input.next();
        if (command.equals("DIM"))
        {
            width = input.nextInt();
            height = input.nextInt();
            rootPane.setPreferredSize(new Dimension(width, height));
        }
    }

    public void makeLine(double x1, double x2, double y1, double y2, double r, double g, double b)
    {
        if (Math.abs((x2 - x1)) >= Math.abs((y2 - y1)))
        {
            double j = (x1 <= x2) ? y1 : y2;
            double m = (x1 <= x2) ? ((y2 - y1) / (x2 - x1)) : (y1 - y2) / (x1 - x2);
            for (double i = ((x1 <= x2) ? x1 : x2); i <= ((x1 >= x2) ? x1 : x2); i++)
            {

                drawPixel((int) i, (int) j, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                j += m;
            }
        }
        else
        {
            double j = (y1 <= y2) ? x1 : x2;
            double m = (y1 <= y2) ? ((x2 - x1) / (y2 - y1)) : ((x1 - x2) / (y1 - y2));
            for (double i = ((y1 <= y2) ? y1 : y2); i <= ((y1 >= y2) ? y1 : y2); i++)
            {
                drawPixel((int) j, (int) i, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                j += m;
            }
        }
    }

    public void makeTriSide(double x1, double y1, double x2, double y2, double r, double g, double b, int[][] scanLine)
    {
        if (Math.abs((x2 - x1)) >= Math.abs((y2 - y1)))
        {
            double j = (x1 <= x2) ? y1 : y2;
            double m = (x1 <= x2) ? ((y2 - y1) / (x2 - x1)) : (y1 - y2) / (x1 - x2);
            for (int i = (int) ((x1 <= x2) ? x1 : x2); i <= ((x1 >= x2) ? x1 : x2); i++)
            {
                if ((int) i < scanLine[(int) j][0])
                {
                    scanLine[(int) j][0] = (int) i;
                }
                if ((int) i > scanLine[(int) j][1])
                {
                    scanLine[(int) j][1] = (int) i;
                }
                drawPixel((int) i, (int) j, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                j += m;
            }
        }
        else
        {
            double j = (y1 <= y2) ? x1 : x2;
            double m = (y1 <= y2) ? ((x2 - x1) / (y2 - y1)) : ((x1 - x2) / (y1 - y2));
            for (int i = (int) ((y1 <= y2) ? y1 : y2); i <= ((y1 >= y2) ? y1 : y2); i++)
            {
                if ((int) j < scanLine[(int) i][0])
                {
                    scanLine[(int) i][0] = (int) j;
                }
                if ((int) j > scanLine[(int) i][1])
                {
                    scanLine[(int) i][1] = (int) j;
                }
                drawPixel((int) j, (int) i, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                j += m;
            }
        }
    }


    public void fillTri(int[][] scanLine, double r, double g, double b)
    {
        for (int i = 0; i < scanLine.length; i++)
        {
            if (scanLine[i][0] <= scanLine[i][1])
            {
                for (int j = scanLine[i][0]; j <= scanLine[i][1]; j++)
                {
                    drawPixel(j, i, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                }
            }
        }
    }


}
