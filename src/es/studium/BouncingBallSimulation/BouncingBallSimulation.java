package es.studium.BouncingBallSimulation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class BouncingBallSimulation extends JFrame 
{
	private static final long serialVersionUID = 1L;
	// Constantes físicas
    private static final double GRAVITY = 9.81; // m/s^2
    private static final double ELASTICITY = 0.8; // Coeficiente de restitución (elasticidad)
    private static final double FRICTION = 0.1; // Coeficiente de fricción

    // Variables de la pelota
    private double height; // Altura de la pelota
    private double velocity; // Velocidad de la pelota
    private double time; // Tiempo de simulación

    // Datos para el gráfico
    private List<Double> times = new ArrayList<>();
    private List<Double> heights = new ArrayList<>();

    // Gráfico
    private XYSeries series;
    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    public BouncingBallSimulation(double initialHeight, double initialVelocity) 
    {
        super("Simulación de la Caída de una Pelota");
        this.height = initialHeight;
        this.velocity = initialVelocity;
        this.time = 0;

        // Inicializar el gráfico
        series = new XYSeries("Altura de la Pelota");
        dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        chart = ChartFactory.createXYLineChart(
                "Simulación de la Caída de una Pelota", // Título del gráfico
                "Tiempo (s)", // Etiqueta del eje X
                "Altura (m)", // Etiqueta del eje Y
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Orientación del gráfico
                true, // Incluir leyenda
                true, // Incluir tooltips
                false // Incluir URLs
        );
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    public void simulate(double timeStep, double initialHeight) 
    {
        while ((height >= 0)&&(time<initialHeight))
        {
            // Actualizar la velocidad y la altura
            velocity += GRAVITY * timeStep;
            height -= velocity * timeStep;

            // Si la pelota toca el suelo, aplicar elasticidad y fricción
            if (height < 0) 
            {
                height = 0;
                velocity = -velocity * ELASTICITY;
                velocity -= FRICTION * velocity;
            }

            // Incrementar el tiempo
            time += timeStep;

            // Almacenar los datos para el gráfico
            times.add(time);
            heights.add(height);

            // Actualizar el gráfico
            series.add(time, height);

            // Imprimir el estado de la pelota
            System.out.printf("Tiempo: %.2f s, Altura: %.2f m, Velocidad: %.2f m/s%n", time, height, velocity);
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> {
            // Altura inicial y velocidad inicial de la pelota
            double initialHeight = 10.0; // 10 metros
            double initialVelocity = 0.0; // 0 m/s

            // Crear una instancia de la simulación
            BouncingBallSimulation simulation = new BouncingBallSimulation(initialHeight, initialVelocity);

            // Ejecutar la simulación con un paso de tiempo de 0.01 segundos
            simulation.simulate(0.01, initialHeight);

            // Mostrar la ventana del gráfico
            simulation.setSize(800, 600);
            simulation.setLocationRelativeTo(null);
            simulation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            simulation.setVisible(true);
        });
    }
}
