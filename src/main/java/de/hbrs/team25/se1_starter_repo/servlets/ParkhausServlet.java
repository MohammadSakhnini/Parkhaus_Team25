package de.hbrs.team25.se1_starter_repo.servlets;

import de.hbrs.team25.se1_starter_repo.classes.Car;
import de.hbrs.team25.se1_starter_repo.classes.CarType;
import de.hbrs.team25.se1_starter_repo.classes.ParkingSpot;
import de.hbrs.team25.se1_starter_repo.classes.Ticket;
import de.hbrs.team25.se1_starter_repo.interfaces.CarIF;
import de.hbrs.team25.se1_starter_repo.interfaces.IParkingSpot;
import de.hbrs.team25.se1_starter_repo.interfaces.ITicket;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * common superclass for all servlets
 * groups all auxiliary common methods used in all servlets
 */
public abstract class ParkhausServlet extends HttpServlet {
    double sum = 0.0;
    int count = 0;
    int electric_count = 0;
    int gas_count = 0;
    double max = 0.0;

    /* abstract methods, to be defined in subclasses */
    abstract String NAME(); // each de.hbrs.team25.se1_starter_repo.servlets.ParkhausServlet should have a name, e.g. "Level1"

    abstract int MAX(); // maximum number of parking slots of a single parking level

    abstract String config(); // configuration of a single parking level

    /**
     * HTTP GET
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String cmd = request.getParameter("cmd");
        System.out.println(cmd + " requested: " + request.getQueryString());
        switch (cmd) {
            case "config":
                // Overwrite Parkhaus config parameters
                // Max, open_from, open_to, delay, simulation_speed
                out.println(config());
                break;
            case "sum":
                // ToDo: insert algorithm for calculating sum here
                out.printf("sum = %.2f €", sum / 100.0);
                break;
            case "avg":
                out.printf("avg = %.2f €", sum / (double) count);
                break;
            case "min":
                // ToDo: insert algorithm for calculating min here
                out.println("min = server side calculated min");
                break;
            case "max":
                out.printf("max  = %.2f €", max / 100.0);
                break;
            case "cars":
                // TODO: Send list of cars stored on the server to the client.
                // Cars are separated by comma.
                // Values of a single car are separated by slash.
                // Format: Nr, timer begin, duration, price, de.hbrs.team25.se1_starter_repo.classes.Ticket, color, space, client category, vehicle type, license (PKW Kennzeichen)
                // For example:
                // TODO replace by real list of cars
                // out.println("1/1648465400000/_/_/Ticket1/#0d1e0a/2/any/PKW/1,2/1648465499999/_/_/Ticket2/#dd10aa/3/any/PKW/2");
                break;
            case "chart":
                response.setContentType("text/plain");
                out.println(String.format("{\n" +
                        "  \"data\": [\n" +
                        "    {\n" +
                        "      \"x\": [\n" +
                        "        \"E-de.hbrs.team25.se1_starter_repo.classes.Car\",\n" +
                        "        \"Gas-de.hbrs.team25.se1_starter_repo.classes.Car\"\n" +
                        "      ],\n" +
                        "      \"y\": [\n" +
                        "        %d,\n" +
                        "        %d\n" +
                        "      ],\n" +
                        "      \"type\": \"bar\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}", electric_count, gas_count));
                break;
            default:
                System.out.println("Invalid Command: " + request.getQueryString());
        }

    }

    /**
     * HTTP POST
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String body = getBody(request);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        System.out.println(body);
        String[] params = body.split(",");
        String event = params[0];
        String[] restParams = Arrays.copyOfRange(params, 1, params.length);

        switch (event) {
            case "enter":

                CarIF newCar = new Car(restParams);
                ITicket newTicket = new Ticket(restParams[4], newCar);
                cars().add(newCar);
                tickets().add(newTicket);

                // System.out.println( "enter," + newCar );
                if (newCar.getCarType() == CarType.Electric) {
                    electric_count++;
                } else {
                    gas_count++;
                }
                count++;


                // re-direct car to another parking lot
                out.println(locator(newCar));
                break;
            case "leave":
                CarIF oldCar = cars().get(0);  // ToDo remove car from list
                double price = 0.0d;
                if (params.length > 4) {
                    String priceString = params[4];
                    if (!"_".equals(priceString)) {
                        price = (double) new Scanner(priceString).useDelimiter("\\D+").nextInt();
                        price /= 100.0d;  // just as Integer.parseInt( priceString ) / 100.0d;
                        // store new sum in ServletContext
                        count++;
                        sum += price;

                        if (price > max) {
                            max = price;
                        }
                        //TODO: getContext().setAttribute("sum" + NAME(), getSum() + price);
                    }
                }
                out.println(price);  // server calculated price
                System.out.println("leave," + oldCar + ", price = " + price);
                break;
            case "invalid":
            case "occupied":
                System.out.println(body);
                break;
            case "tomcat":
                out.println(getServletConfig().getServletContext().getServerInfo()
                        + getServletConfig().getServletContext().getMajorVersion()
                        + getServletConfig().getServletContext().getMinorVersion());
                break;
            default:
                System.out.println(body);
                // System.out.println( "Invalid Command: " + body );
        }

    }


    // auxiliary methods used in HTTP request processing

    /**
     * @return the servlet context
     */
    ServletContext getContext() {
        return getServletConfig().getServletContext();
    }

    /**
     * TODO: replace this by your own function
     *
     * @return the number of the free parking lot to which the next incoming car will be directed
     */
    int locator(CarIF car) {
        // numbers of parking lots start at 1, not zero
        // return 1;  // always use the first space
        return 1 + ((cars().size() - 1) % this.MAX());
    }

    /**
     * @return the list of all cars stored in the servlet context so far
     */
    @SuppressWarnings("unchecked")
    List<CarIF> cars() {
        if (getContext().getAttribute("cars" + NAME()) == null) {
            getContext().setAttribute("cars" + NAME(), new ArrayList<Car>());
        }
        return (List<CarIF>) getContext().getAttribute("cars" + NAME());
    }


    /**
     * @return the list of all tickets stored in the servlet context so far
     */
    @SuppressWarnings("unchecked")
    List<ITicket> tickets() {
        if (getContext().getAttribute("tickets" + NAME()) == null) {
            getContext().setAttribute("tickets" + NAME(), new ArrayList<Ticket>());
        }
        return (List<ITicket>) getContext().getAttribute("tickets" + NAME());
    }


    /**
     * @return the list of all tickets stored in the servlet context so far
     */
    @SuppressWarnings("unchecked")
    List<IParkingSpot> parkingSpots() {
        if (getContext().getAttribute("parkingSpots" + NAME()) == null) {
            getContext().setAttribute("parkingSpots" + NAME(), new ArrayList<ParkingSpot>());
        }
        return (List<IParkingSpot>) getContext().getAttribute("parkingSpots" + NAME());
    }

    /**
     * @param request the HTTP POST request
     * @return the body of the request
     */
    String getBody(HttpServletRequest request) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public void destroy() {
        System.out.println("Servlet destroyed.");
    }
}