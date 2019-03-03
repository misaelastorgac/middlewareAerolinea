/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.middleware;

import java.net.*;
import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import org.json.*;
import org.yaml.snakeyaml.Yaml;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Element;


/**
 *
 * @author misae
 */
public class appMiddle extends Thread {

    Socket servicio = null;
    DataInputStream flujoDatosEntrada = null;
    DataOutputStream flujoDatosSalida = null;

    public appMiddle(Socket servicio, DataInputStream x, DataOutputStream y) {  //Constructor

        this.servicio = servicio;
        flujoDatosEntrada = x;
        flujoDatosSalida = y;
    }

    public void run() {  //Esto es un metodo, que es lo que correra cada hilo de nustro servidor

        System.out.println("Se acepto una nueva conexion");

        try {

            String nuevaEntrada = flujoDatosEntrada.readUTF();

            int estado = saberEstado(nuevaEntrada);

            //Decidiendo a quien va. Si el estado es 0, el mensaje va a las aerolineas. Si el estado es 1, el mensaje va hacia el cliente
            if (estado == 0) {
                System.out.println("Estado en 0");
                String json = convertirXMLaJSON(nuevaEntrada);
                enviarAerolineaUno(json);
                enviarAerolineaDos(json);

            }
            if (estado == 1) {
                System.out.println("Estado en 1");
                String xml = convertirJSONaXML(nuevaEntrada);
                enviarCliente(xml);

            }
            if (estado == 2) {
                System.out.println("Estado en 2");
                String json = convertirXMLaJSON(nuevaEntrada);
                enviarAerolineaUno(json);
                enviarAerolineaDos(json);

            }

        } catch (Exception e) {

        }

    }

    public void enviarAerolineaUno(String entrada) {
        String ip = "177.230.91.100";
        int PUERTO = 5555;

        try {
            servicio = new Socket(ip, PUERTO);
            flujoDatosSalida = new DataOutputStream(servicio.getOutputStream());//Creamos objeto para enviar
            flujoDatosSalida.writeUTF(entrada);  //Mandamos el mensaje al servidor

        } catch (Exception e) {

            System.out.println("No se puedo crear la conexion");
            e.printStackTrace();
        }

    }

    public void enviarAerolineaDos(String entrada) {
        String ip = "177.230.91.100";
        int PUERTO = 5555;

        try {
            servicio = new Socket(ip, PUERTO);
            flujoDatosSalida = new DataOutputStream(servicio.getOutputStream());//Creamos objeto para enviar
            flujoDatosSalida.writeUTF(entrada);  //Mandamos el mensaje al servidor

        } catch (Exception e) {

            System.out.println("No se puedo crear la conexion");
            e.printStackTrace();
        }
    }

    public void enviarCliente(String entrada) {
        String ip = ".";
        int PUERTO = 5555;

        try {
            servicio = new Socket(ip, PUERTO);
            flujoDatosSalida = new DataOutputStream(servicio.getOutputStream());//Creamos objeto para enviar
            flujoDatosSalida.writeUTF(entrada);  //Mandamos el mensaje al servidor

        } catch (Exception e) {

            System.out.println("No se puedo crear la conexion");
            e.printStackTrace();
        }

    }

    public int saberEstado(String entrada) throws ParserConfigurationException, XPathExpressionException {

        char x = entrada.charAt(0);

        if (x == '{') {
            JSONObject jsonObject2 = new JSONObject(entrada);
            return jsonObject2.getInt("Estado");
        }

        if (x == '<') {
           return 0;
            
        }

        return 0;

    }

    public String convertirXMLaJSON(String entrada) {
        JSONObject jsonObject = XML.toJSONObject(entrada);
        String salida = jsonObject.toString(1);

        return salida;
    }

    public String convertirJSONaXML(String entrada) {
        JSONObject json = new JSONObject(entrada);
        String xml = XML.toString(json);

        return xml;
    }

    public String convertirJSONaYAML(String json) {

        JSONObject jsonObject2 = new JSONObject(json);

        Yaml yaml = new Yaml();
        //Viajes viaje = new Viajes(jsonObject2.getString("Origen"), jsonObject2.getString("Origen"), jsonObject2.getString("Ida"), jsonObject2.getString("Vuelta"), jsonObject2.getInt("Estado"));

        //String viajeYAML = yaml.dump(viaje);
        return null;

    }

}
