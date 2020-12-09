import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.*;
import java.awt.event.KeyListener;
import javax.swing.*;

import java.io.*;
import java.util.*;
import java.lang.Object;


public class TCSS458Paint extends JPanel
{
    static int width;
    static int height;
    static double xr = 0;
    static double yr = 0;
    int imageSize;
    int[] pixels;
    double[][] CTM;
    double[][] zBuffer;


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
        double[][] scanLine = new double[height][4];

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
                CTM = new double[4][4];
                CTM[0][0] = 1;
                CTM[1][1] = 1;
                CTM[2][2] = 1;
                CTM[3][3] = 1;
                zBuffer = new double[width][height];
                for (int i = 0; i < zBuffer.length; i++)
                {
                    for (int j = 0;  j < zBuffer[i].length; j++)
                    {
                        zBuffer[i][j] = Integer.MIN_VALUE;
                    }
                }
                for (int i = 0; i < pixels.length; i++)
                {
                    pixels[i] = 255;
                }
            }
            else if (command.equals("LINE"))
            {
                double xSub1 = input.nextDouble();
                double ySub1 = input.nextDouble();
                double z1 = input.nextDouble();
                double xSub2 = input.nextDouble();
                double ySub2 = input.nextDouble();
                double z2 = input.nextDouble();
                double x1 = (width - 1) * (xSub1 + 1) / 2;
                double x2 = (width - 1) * (xSub2 + 1) / 2;
                double y1 = (height - 1) * (ySub1 + 1) / 2;
                double y2 = (height - 1) * (ySub2 + 1) / 2;
                makeLine(x1, x2, y1, y2, z1, z2, r, g, b);
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
                double z1 = input.nextDouble();
                double xSub2 = input.nextDouble();
                double ySub2 = input.nextDouble();
                double z2 = input.nextDouble();
                double xSub3 = input.nextDouble();
                double ySub3 = input.nextDouble();
                double z3 = input.nextDouble();
                double x1 = (xSub1 * CTM[0][0]) + (ySub1 * CTM[0][1]) + (z1 * CTM[0][2]) + (CTM[0][3]);
                double x2 = (xSub2 * CTM[0][0]) + (ySub2 * CTM[0][1]) + (z2 * CTM[0][2]) + (CTM[0][3]);
                double x3 = (xSub3 * CTM[0][0]) + (ySub3 * CTM[0][1]) + (z3 * CTM[0][2]) + (CTM[0][3]);
                double y1 = (xSub1 * CTM[1][0]) + (ySub1 * CTM[1][1]) + (z1 * CTM[1][2]) + (CTM[1][3]);
                double y2 = (xSub2 * CTM[1][0]) + (ySub2 * CTM[1][1]) + (z2 * CTM[1][2]) + (CTM[1][3]);
                double y3 = (xSub3 * CTM[1][0]) + (ySub3 * CTM[1][1]) + (z3 * CTM[1][2]) + (CTM[1][3]);
                z1 = (xSub1 * CTM[2][0]) + (ySub1 * CTM[2][1]) + (z1 * CTM[2][2]) + (CTM[2][3]);
                z2 = (xSub2 * CTM[2][0]) + (ySub2 * CTM[2][1]) + (z2 * CTM[2][2]) + (CTM[2][3]);
                z3 = (xSub3 * CTM[2][0]) + (ySub3 * CTM[2][1]) + (z3 * CTM[2][2]) + (CTM[2][3]);
                xSub1 = x1;
                xSub2 = x2;
                xSub3 = x3;
                ySub1 = y1;
                ySub2 = y2;
                ySub3 = y3;
                double[][] rX = new double[4][4];
                rX[0][0] = 1;
                rX[3][3] = 1;
                rX[1][1] = Math.cos(Math.toRadians(xr));
                rX[1][2] = -Math.sin(Math.toRadians(xr));
                rX[2][1] = Math.sin(Math.toRadians(xr));
                rX[2][2] = Math.cos(Math.toRadians(xr));
                x1 = (xSub1 * rX[0][0]) + (ySub1 * rX[0][1]) + (z1 * rX[0][2]) + (rX[0][3]);
                x2 = (xSub2 * rX[0][0]) + (ySub2 * rX[0][1]) + (z2 * rX[0][2]) + (rX[0][3]);
                x3 = (xSub3 * rX[0][0]) + (ySub3 * rX[0][1]) + (z3 * rX[0][2]) + (rX[0][3]);
                y1 = (xSub1 * rX[1][0]) + (ySub1 * rX[1][1]) + (z1 * rX[1][2]) + (rX[1][3]);
                y2 = (xSub2 * rX[1][0]) + (ySub2 * rX[1][1]) + (z2 * rX[1][2]) + (rX[1][3]);
                y3 = (xSub3 * rX[1][0]) + (ySub3 * rX[1][1]) + (z3 * rX[1][2]) + (rX[1][3]);
                z1 = (xSub1 * rX[2][0]) + (ySub1 * rX[2][1]) + (z1 * rX[2][2]) + (rX[2][3]);
                z2 = (xSub2 * rX[2][0]) + (ySub2 * rX[2][1]) + (z2 * rX[2][2]) + (rX[2][3]);
                z3 = (xSub3 * rX[2][0]) + (ySub3 * rX[2][1]) + (z3 * rX[2][2]) + (rX[2][3]);
                xSub1 = x1;
                xSub2 = x2;
                xSub3 = x3;
                ySub1 = y1;
                ySub2 = y2;
                ySub3 = y3;
                double[][] rY = new double[4][4];
                rY[0][0] = Math.cos(Math.toRadians(yr));
                rY[3][3] = 1;
                rY[1][1] = 1;
                rY[0][2] = Math.sin(Math.toRadians(yr));
                rY[2][0] = -Math.sin(Math.toRadians(yr));
                rY[2][2] = Math.cos(Math.toRadians(yr));
                x1 = (xSub1 * rY[0][0]) + (ySub1 * rY[0][1]) + (z1 * rY[0][2]) + (rY[0][3]);
                x2 = (xSub2 * rY[0][0]) + (ySub2 * rY[0][1]) + (z2 * rY[0][2]) + (rY[0][3]);
                x3 = (xSub3 * rY[0][0]) + (ySub3 * rY[0][1]) + (z3 * rY[0][2]) + (rY[0][3]);
                y1 = (xSub1 * rY[1][0]) + (ySub1 * rY[1][1]) + (z1 * rY[1][2]) + (rY[1][3]);
                y2 = (xSub2 * rY[1][0]) + (ySub2 * rY[1][1]) + (z2 * rY[1][2]) + (rY[1][3]);
                y3 = (xSub3 * rY[1][0]) + (ySub3 * rY[1][1]) + (z3 * rY[1][2]) + (rY[1][3]);
                z1 = (xSub1 * rY[2][0]) + (ySub1 * rY[2][1]) + (z1 * rY[2][2]) + (rY[2][3]);
                z2 = (xSub2 * rY[2][0]) + (ySub2 * rY[2][1]) + (z2 * rY[2][2]) + (rY[2][3]);
                z3 = (xSub3 * rY[2][0]) + (ySub3 * rY[2][1]) + (z3 * rY[2][2]) + (rY[2][3]);
                x1 = ((width - 1) * (x1 + 1) / 2);
                x2 = ((width - 1) * (x2 + 1) / 2);
                x3 = ((width - 1) * (x3 + 1) / 2);
                y1 = ((height - 1) * (y1 + 1) / 2);
                y2 = ((height - 1) * (y2 + 1) / 2);
                y3 = ((height - 1) * (y3 + 1) / 2);
                makeTriSide(x1, y1, z1, x2, y2, z2, r, g, b, scanLine);
                makeTriSide(x1, y1, z1, x3, y3, z3, r, g, b, scanLine);
                makeTriSide(x2, y2, z2, x3, y3, z3, r, g, b, scanLine);
                fillTri(scanLine, r, g, b);
            }
            else if (command.equals("LOAD_IDENTITY_MATRIX"))
            {
                for (int i = 0; i < CTM.length; i++)
                {
                    for (int j = 0; j < CTM[i].length; j++)
                    {
                        if (i == j)
                        {
                            CTM[i][j] = 1;
                        }
                        else
                        {
                            CTM[i][j] = 0;
                        }
                    }
                }

            }
            else if (command.equals("TRANSLATE"))
            {
                double x = input.nextDouble();
                double y = input.nextDouble();
                double z = input.nextDouble();
                double[][] translate = new double[4][4];
                double[][] CTMSub = new double[4][4];
                for (int i = 0; i < CTM.length; i++)
                {
                    for (int j = 0; j < CTM[i].length; j++)
                    {
                        CTMSub[i][j] = CTM[i][j];
                    }
                }
                translate[0][0] = 1;
                translate[1][1] = 1;
                translate[2][2] = 1;
                translate[3][3] = 1;
                translate[0][3] = x;
                translate[1][3] = y;
                translate[2][3] = z;

                for (int i = 0; i < translate.length; i++)
                {
                    for (int j = 0; j < translate[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < translate[i].length; k++)
                        {
                            t += translate[i][k] * CTMSub[k][j];
                        }
                        CTM[i][j] = t;
                    }
                }
            }
            else if (command.equals("ROTATEX"))
            {
                double x = input.nextDouble();
                double[][] rotateX = new double[4][4];
                double[][] CTMSub = new double[4][4];
                for (int i = 0; i < CTM.length; i++)
                {
                    for (int j = 0; j < CTM[i].length; j++)
                    {
                        CTMSub[i][j] = CTM[i][j];
                    }
                }
                rotateX[0][0] = 1;
                rotateX[3][3] = 1;
                rotateX[1][1] = Math.cos(Math.toRadians(x));
                rotateX[1][2] = -Math.sin(Math.toRadians(x));
                rotateX[2][1] = Math.sin(Math.toRadians(x));
                rotateX[2][2] = Math.cos(Math.toRadians(x));
                for (int i = 0; i < rotateX.length; i++)
                {
                    for (int j = 0; j < rotateX[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < rotateX[i].length; k++)
                        {
                            t += rotateX[i][k] * CTMSub[k][j];
                        }
                        CTM[i][j] = t;
                    }
                }
            }
            else if (command.equals("ROTATEY"))
            {
                double x = input.nextDouble();
                double[][] rotateY = new double[4][4];
                double[][] CTMSub = new double[4][4];
                for (int i = 0; i < CTM.length; i++)
                {
                    for (int j = 0; j < CTM[i].length; j++)
                    {
                        CTMSub[i][j] = CTM[i][j];
                    }
                }
                rotateY[0][0] = Math.cos(Math.toRadians(x));
                rotateY[3][3] = 1;
                rotateY[1][1] = 1;
                rotateY[0][2] = Math.sin(Math.toRadians(x));
                rotateY[2][0] = -Math.sin(Math.toRadians(x));
                rotateY[2][2] = Math.cos(Math.toRadians(x));
                for (int i = 0; i < rotateY.length; i++)
                {
                    for (int j = 0; j < rotateY[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < rotateY[i].length; k++)
                        {
                            t += rotateY[i][k] * CTMSub[k][j];
                        }
                        CTM[i][j] = t;
                    }
                }
            }
            else if (command.equals("ROTATEZ"))
            {
                double x = input.nextDouble();
                double[][] rotateZ = new double[4][4];
                double[][] CTMSub = new double[4][4];
                for (int i = 0; i < CTM.length; i++)
                {
                    for (int j = 0; j < CTM[i].length; j++)
                    {
                        CTMSub[i][j] = CTM[i][j];
                    }
                }
                rotateZ[0][0] = Math.cos(Math.toRadians(x));
                rotateZ[0][1] = -Math.sin(Math.toRadians(x));
                rotateZ[1][0] = Math.sin(Math.toRadians(x));
                rotateZ[1][1] = Math.cos(Math.toRadians(x));
                rotateZ[2][2] = 1;
                rotateZ[3][3] = 1;
                for (int i = 0; i < rotateZ.length; i++)
                {
                    for (int j = 0; j < rotateZ[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < rotateZ[i].length; k++)
                        {
                            t += rotateZ[i][k] * CTMSub[k][j];
                        }
                        CTM[i][j] = t;
                    }
                }
            }
            else if (command.equals("SCALE"))
            {
                double x = input.nextDouble();
                double y = input.nextDouble();
                double z = input.nextDouble();
                double[][] scale = new double[4][4];
                double[][] CTMSub = new double[4][4];
                for (int i = 0; i < CTM.length; i++)
                {
                    for (int j = 0; j < CTM[i].length; j++)
                    {
                        CTMSub[i][j] = CTM[i][j];
                    }
                }
                scale[0][0] = x;
                scale[1][1] = y;
                scale[2][2] = z;
                scale[3][3] = 1;
                for (int i = 0; i < scale.length; i++)
                {
                    for (int j = 0; j < scale[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < scale[i].length; k++)
                        {
                            t += scale[i][k] * CTMSub[k][j];
                        }
                        CTM[i][j] = t;
                    }
                }
            }
            else if (command.equals("WIREFRAME_CUBE"))
            {
                double[][] points = new double[4][8];

                points[0][0] = .5;
                points[1][0] = .5;
                points[2][0] = .5;

                points[0][1] = .5;
                points[1][1] = .5;
                points[2][1] = -.5;

                points[0][2] = .5;
                points[1][2] = -.5;
                points[2][2] = .5;

                points[0][3] = .5;
                points[1][3] = -.5;
                points[2][3] = -.5;

                points[0][4] = -.5;
                points[1][4] = .5;
                points[2][4] = .5;

                points[0][5] = -.5;
                points[1][5] = .5;
                points[2][5] = -.5;

                points[0][6] = -.5;
                points[1][6] = -.5;
                points[2][6] = .5;

                points[0][7] = -.5;
                points[1][7] = -.5;
                points[2][7] = -.5;

                for (int i = 0; i < points[3].length; i++)
                {
                    points[3][i] = 1;
                }
                double[][] pointsSub = new double[4][8];
                for (int i = 0; i < points.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        pointsSub[i][j] = points[i][j];
                    }
                }

                for (int i = 0; i < CTM.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < CTM[i].length; k++)
                        {
                            t += CTM[i][k] * pointsSub[k][j];
                        }
                        points[i][j] = t;
                    }
                }

                for (int i = 0; i < points.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        pointsSub[i][j] = points[i][j];
                    }
                }

                double[][] rX = new double[4][4];
                rX[0][0] = 1;
                rX[3][3] = 1;
                rX[1][1] = Math.cos(Math.toRadians(xr));
                rX[1][2] = -Math.sin(Math.toRadians(xr));
                rX[2][1] = Math.sin(Math.toRadians(xr));
                rX[2][2] = Math.cos(Math.toRadians(xr));
                for (int i = 0; i < rX.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < points.length; k++)
                        {
                            t += rX[i][k] * pointsSub[k][j];
                        }
                        points[i][j] = t;
                    }
                }

                for (int i = 0; i < points.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        pointsSub[i][j] = points[i][j];
                    }
                }
                double[][] rY = new double[4][4];
                rY[0][0] = Math.cos(Math.toRadians(yr));
                rY[3][3] = 1;
                rY[1][1] = 1;
                rY[0][2] = Math.sin(Math.toRadians(yr));
                rY[2][0] = -Math.sin(Math.toRadians(yr));
                rY[2][2] = Math.cos(Math.toRadians(yr));
                for (int i = 0; i < rY.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < points.length; k++)
                        {
                            t += rY[i][k] * pointsSub[k][j];
                        }
                        points[i][j] = t;
                    }
                }

                for (int i = 0; i < points[0].length; i++)
                {
                    points[0][i] = ((width - 1) * (points[0][i] + 1) / 2);
                    points[1][i] = ((height - 1) * (points[1][i] + 1) / 2);
                }
                makeLine(points[0][0], points[0][1], points[1][0], points[1][1], points[2][0], points[2][1], r, g, b);
                makeLine(points[0][0], points[0][2], points[1][0], points[1][2], points[2][0], points[2][2], r, g, b);
                makeLine(points[0][0], points[0][4], points[1][0], points[1][4], points[2][0], points[2][4], r, g, b);
                makeLine(points[0][3], points[0][7], points[1][3], points[1][7], points[2][3], points[2][7], r, g, b);
                makeLine(points[0][5], points[0][7], points[1][5], points[1][7], points[2][5], points[2][7], r, g, b);
                makeLine(points[0][6], points[0][7], points[1][6], points[1][7], points[2][6], points[2][7], r, g, b);
                makeLine(points[0][4], points[0][6], points[1][4], points[1][6], points[2][4], points[2][6], r, g, b);
                makeLine(points[0][4], points[0][5], points[1][4], points[1][5], points[2][4], points[2][5], r, g, b);
                makeLine(points[0][1], points[0][5], points[1][1], points[1][5], points[2][1], points[2][5], r, g, b);
                makeLine(points[0][1], points[0][3], points[1][1], points[1][3], points[2][1], points[2][3], r, g, b);
                makeLine(points[0][2], points[0][3], points[1][2], points[1][3], points[2][2], points[2][3], r, g, b);
                makeLine(points[0][2], points[0][6], points[1][2], points[1][6], points[2][2], points[2][6], r, g, b);
            }
            else if (command.equals("SOLID_CUBE"))
            {
                double[][] points = new double[4][8];

                points[0][0] = .5;
                points[1][0] = .5;
                points[2][0] = .5;

                points[0][1] = .5;
                points[1][1] = .5;
                points[2][1] = -.5;

                points[0][2] = .5;
                points[1][2] = -.5;
                points[2][2] = .5;

                points[0][3] = .5;
                points[1][3] = -.5;
                points[2][3] = -.5;

                points[0][4] = -.5;
                points[1][4] = .5;
                points[2][4] = .5;

                points[0][5] = -.5;
                points[1][5] = .5;
                points[2][5] = -.5;

                points[0][6] = -.5;
                points[1][6] = -.5;
                points[2][6] = .5;

                points[0][7] = -.5;
                points[1][7] = -.5;
                points[2][7] = -.5;

                for (int i = 0; i < points[3].length; i++)
                {
                    points[3][i] = 1;
                }
                double[][] pointsSub = new double[4][8];
                for (int i = 0; i < points.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        pointsSub[i][j] = points[i][j];
                    }
                }

                for (int i = 0; i < CTM.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < CTM[i].length; k++)
                        {
                            t += CTM[i][k] * pointsSub[k][j];
                        }
                        points[i][j] = t;
                    }
                }

                for (int i = 0; i < points.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        pointsSub[i][j] = points[i][j];
                    }
                }

                double[][] rX = new double[4][4];
                rX[0][0] = 1;
                rX[3][3] = 1;
                rX[1][1] = Math.cos(Math.toRadians(xr));
                rX[1][2] = -Math.sin(Math.toRadians(xr));
                rX[2][1] = Math.sin(Math.toRadians(xr));
                rX[2][2] = Math.cos(Math.toRadians(xr));
                for (int i = 0; i < rX.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < points.length; k++)
                        {
                            t += rX[i][k] * pointsSub[k][j];
                        }
                        points[i][j] = t;
                    }
                }

                for (int i = 0; i < points.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        pointsSub[i][j] = points[i][j];
                    }
                }
                double[][] rY = new double[4][4];
                rY[0][0] = Math.cos(Math.toRadians(yr));
                rY[3][3] = 1;
                rY[1][1] = 1;
                rY[0][2] = Math.sin(Math.toRadians(yr));
                rY[2][0] = -Math.sin(Math.toRadians(yr));
                rY[2][2] = Math.cos(Math.toRadians(yr));
                for (int i = 0; i < rY.length; i++)
                {
                    for (int j = 0; j < points[i].length; j++)
                    {
                        double t = 0;
                        for (int k = 0; k < points.length; k++)
                        {
                            t += rY[i][k] * pointsSub[k][j];
                        }
                        points[i][j] = t;
                    }
                }

                for (int i = 0; i < points[0].length; i++)
                {
                    points[0][i] = ((width - 1) * (points[0][i] + 1) / 2);
                    points[1][i] = ((height - 1) * (points[1][i] + 1) / 2);
                }
                makeTriSide(points[0][0], points[1][0], points[2][0], points[0][1], points[1][1], points[2][1], r, g, b, scanLine);
                makeTriSide(points[0][0], points[1][0], points[2][0], points[0][2], points[1][2], points[2][2], r, g, b, scanLine);
                makeTriSide(points[0][0], points[1][0], points[2][0], points[0][4], points[1][4], points[2][4], r, g, b, scanLine);
                makeTriSide(points[0][3], points[1][3], points[2][3], points[0][7], points[1][7], points[2][7], r, g, b, scanLine);
                makeTriSide(points[0][5], points[1][5], points[2][5], points[0][7], points[1][7], points[2][7], r, g, b, scanLine);
                makeTriSide(points[0][6], points[1][6], points[2][6], points[0][7], points[1][7], points[2][7], r, g, b, scanLine);
                makeTriSide(points[0][4], points[1][4], points[2][4], points[0][6], points[1][6], points[2][6], r, g, b, scanLine);
                makeTriSide(points[0][4], points[1][4], points[2][4], points[0][5], points[1][5], points[2][5], r, g, b, scanLine);
                makeTriSide(points[0][1], points[1][1], points[2][1], points[0][5], points[1][5], points[2][5], r, g, b, scanLine);
                makeTriSide(points[0][1], points[1][1], points[2][1], points[0][3], points[1][3], points[2][3], r, g, b, scanLine);
                makeTriSide(points[0][2], points[1][2], points[2][2], points[0][3], points[1][3], points[2][3], r, g, b, scanLine);
                makeTriSide(points[0][2], points[1][2], points[2][2], points[0][6], points[1][6], points[2][6], r, g, b, scanLine);
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
        frame.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent keyEvent)
            {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent)
            {
                int key = keyEvent.getKeyCode();
                if (key == KeyEvent.VK_LEFT)
                {
                    yr -= 3;

                }
                else if (key == KeyEvent.VK_RIGHT)
                {
                    yr += 3;
                }
                else if (key == KeyEvent.VK_UP)
                {
                    xr -= 3;
                }
                else if (key == KeyEvent.VK_DOWN)
                {
                    xr += 3;
                }
                frame.repaint();
            }

            @Override
            public void keyReleased(KeyEvent keyEvent)
            {

            }
        });
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

    public void makeLine(double x1, double x2, double y1, double y2, double z1, double z2, double r, double g, double b)
    {
        if (Math.abs((x2 - x1)) >= Math.abs((y2 - y1)))
        {
            double j = (x1 <= x2) ? y1 : y2;
            double k = (x1 <= x2) ? z1 : z2;
            double m1 = (x1 <= x2) ? ((y2 - y1) / (x2 - x1)) : (y1 - y2) / (x1 - x2);
            double m2 = (x1 <= x2) ? ((z2 - z1) / (x2 - x1)) : (z1 - z2) / (x1 - x2);
            for (int i =  (int) ((x1 <= x2) ? x1 : x2); i <= ((x1 >= x2) ? x1 : x2); i++)
            {
                if (k > zBuffer[i][(int) j])
                {
                    drawPixel(i, (int) j, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                    zBuffer[i][(int) j] = k;
                }
                j += m1;
                k += m2;
            }
        }
        else
        {
            double j = (y1 <= y2) ? x1 : x2;
            double k = (y1 <= y2) ? z1 : z2;
            double m1 = (y1 <= y2) ? ((x2 - x1) / (y2 - y1)) : ((x1 - x2) / (y1 - y2));
            double m2 = (y1 <= y2) ? ((z2 - z1) / (y2 - y1)) : (z1 - z2) / (y1 - y2);
            for (int i = (int) ((y1 <= y2) ? y1 : y2); i <= ((y1 >= y2) ? y1 : y2); i++)
            {
                if (k > zBuffer[(int) j][(int) i])
                {
                    drawPixel((int) j, i, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                    zBuffer[(int) j][i] = k;
                }
                j += m1;
                k += m2;
            }
        }
    }

    public void makeTriSide(double x1, double y1, double z1, double x2, double y2, double z2, double r, double g, double b, double[][] scanLine)
    {
        if (Math.abs((x2 - x1)) >= Math.abs((y2 - y1)))
        {
            double j = (x1 <= x2) ? y1 : y2;
            double k = (x1 <= x2) ? z1 : z2;
            double m1 = (x1 <= x2) ? ((y2 - y1) / (x2 - x1)) : (y1 - y2) / (x1 - x2);
            double m2 = (x1 <= x2) ? ((z2 - z1) / (x2 - x1)) : (z1 - z2) / (x1 - x2);
            for (int i = (int) ((x1 <= x2) ? x1 : x2); i <= ((x1 >= x2) ? x1 : x2); i++)
            {
                if (i < scanLine[(int) j][0])
                {
                    scanLine[(int) j][0] = i;
                    scanLine[(int) j][2] = k;
                }
                if (i > scanLine[(int) j][1])
                {
                    scanLine[(int) j][1] = (int) i;
                    scanLine[(int) j][3] = k;
                }
                if (k > zBuffer[i][(int) j])
                {
                    drawPixel(i, (int) j, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                    zBuffer[i][(int) j] = k;
                }

                j += m1;
                k += m2;
            }
        }
        else
        {
            double j = (y1 <= y2) ? x1 : x2;
            double k = (y1 <= y2) ? z1 : z2;
            double m1 = (y1 <= y2) ? ((x2 - x1) / (y2 - y1)) : ((x1 - x2) / (y1 - y2));
            double m2 = (y1 <= y2) ? ((z2 - z1) / (y2 - y1)) : (z1 - z2) / (y1 - y2);
            for (int i = (int) ((y1 <= y2) ? y1 : y2); i <= ((y1 >= y2) ? y1 : y2); i++)
            {
                if ((int) j < scanLine[(int) i][0])
                {
                    scanLine[(int) i][0] = (int) j;
                    scanLine[(int) i][2] = k;
                }
                if ((int) j > scanLine[(int) i][1])
                {
                    scanLine[(int) i][1] = (int) j;
                    scanLine[(int) i][3] = k;
                }
                if (k > zBuffer[(int) j][(int) i])
                {
                    drawPixel((int) j, (int) i, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                    zBuffer[(int) j][(int) i] = k;
                }

                j += m1;
                k += m2;
            }
        }
    }


    public void fillTri(double[][] scanLine, double r, double g, double b)
    {
        for (int i = 0; i < scanLine.length; i++)
        {
            if (scanLine[i][0] <= scanLine[i][1])
            {
                double k = scanLine[i][2];
                double m = (scanLine[i][3] - scanLine[i][2]) / (scanLine[i][1] - scanLine[i][0]);
                for (double j =  scanLine[i][0]; j <= scanLine[i][1]; j++)
                {
                    if (k > zBuffer[(int) Math.round(j)][i])
                    {
                        drawPixel((int) Math.round(j), i, (int) (r * 255), (int) (g * 255), (int) (b * 255));
                        zBuffer[(int) Math.round(j)][i] = k;
                    }
                    k += m;
                }
            }
        }
    }


}
