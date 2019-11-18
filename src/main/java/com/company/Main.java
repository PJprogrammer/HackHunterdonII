package main.java.com.company;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Main extends AbstractAnalysis {
    //Server Variables
    private static final String IP = "10.0.0.9";
    private static final int PORT = 8000;
    private Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    public Main() {
        try {
            socket = new Socket(IP, PORT);
            System.out.println("Connected");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        int size = 1;

        Coord3d[] points = new Coord3d[size];
        Color[]   colors = new Color[size];
        points[0] = new Coord3d(0,0,0);
        colors[0] = new Color(0,0,0);

        Scatter scatter = new Scatter(points, colors);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        chart.getScene().add(scatter);

        //Add initial point
        Point point = new Point(new Coord3d(1, 1, 1), new Color(255, 255, 255));
        chart.getScene().getGraph().add(point);
    }

    public void readData() {
        String dataString = "";

        try {
            while(true) {
                while ((dataString = bufferedReader.readLine()) != null) {
                    float x = 0;
                    float y = 0;
                    float z = 0;
                    try {
                        x = Float.parseFloat(dataString.split(",")[0]);
                        y = Float.parseFloat(dataString.split(",")[1]);
                        z = Float.parseFloat(dataString.split(",")[2]);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    if(x < 10 && y < 10 && x > -10 && y > -10) {
                        System.out.println(x + "," + y + "," + z);
                        chart.getScene().getGraph().add(new Point(new Coord3d(x, y, z), new Color(66, 134, 244),10));
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public static void main(String[] args) {
        Main main = new Main();
        try {
            AnalysisLauncher.open(main);
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.readData();
    }
}
