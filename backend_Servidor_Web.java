package controllers;

import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.*;
import models.*;
import com.google.gson.stream.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.mail.search.BodyTerm;
import java.util.List;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class Application extends Controller
{
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void index()
    {
        renderTemplate("Application/init.html");
    }

    public static void LoginHTML()
    {
        renderTemplate("Application/login.html");
    }

    public static void RegisterHTML()
    {
        renderTemplate("Application/register.html");
    }

    public static void HomeHTML()
    {
        renderTemplate("Application/home.html");
    }

    public static void startDB()
    {
        USER ob1 = new USER("pep", "1234", 3, 1);
        ob1.save();
        SPACEMEETING ob3 = new SPACEMEETING(4, "Monday-9h:21h","Castelldefels",  true);
        ob3.save();
        MEETING ob2 = new MEETING(  3, "4/10/24-12h00");
        ob2.save();
    }

    public static void Register(String n, String p)
    {
        USER u = USER.find("byNameAndPassword", n, p).first();

        if (u == null)
        {
            USER user = new USER(n, p, 0, 0).save();
            renderArgs.put("ConnectedUser", user);
            HomeHTML();
        }
        else
        {
            RegisterHTML();
        }
    }

    public static void Login(String n, String p)
    {
        USER u = USER.find("byNameAndPassword", n, p).first();

        if (u == null)
        {
            String er = "true";
            renderArgs.put("error", er);
            LoginHTML();
        }
        else
        {
            renderArgs.put("ConnectedUser", u);
            HomeHTML();
        }
    }

    public static void RegisterApp(String n, String p){

        //String username = params.get("username");
        //String password = params.get("password");

        System.out.printf("Name register: %s", n);
        System.out.printf("Password register: %s", p);

        //los dos parametros restantes sera 0 por defecto, ya que el cliente es nuevo
        USER c = USER.find("byNameAndPassword", n, p).first();

        if (c == null) {
            new USER(n, p, 0, 0).save();
            //renderText("Usuario dado de alta");//envia mensaje al cliente
            renderText("0");
        }else{
            //renderText("Usuario existente");
            renderText("-1");
        }

    }

    public static void Home()
    {

    }

    public static  void InitLog()
    {
        String er = "false";
        renderArgs.put("error", er);
        LoginHTML();
    }
    public static  void InitReg()
    {
        RegisterHTML();
    }

    public static void LoginApp(String n, String p){
        USER c = USER.find("byNameAndPassword", n,p).first();

        System.out.printf("Name login: %s", n);
        System.out.printf("Password login: %s", p);

        if (c == null) {
            renderText("-1");
        }else{
            renderText(c.id);
        }
    }


    public static void listSpaces(){
        List<SPACEMEETING> spaceses = SPACEMEETING.findAll();
        render("Application/listSpace.html", spaceses);

    }



    public static void listSpacesAndroid(){
    List<SPACEMEETING> spaceses = SPACEMEETING.findAll();

    List<SpaceMeetingDTO> spaceDTOs = new ArrayList<>();
    for (SPACEMEETING space : spaceses) {
        spaceDTOs.add(new SpaceMeetingDTO(space));
    }
    renderJSON(spaceDTOs);
}

    public static void addSpaceAndroid(int n, String schedule, String location, String ventilation, long id) {
        USER user = USER.find("byId", id).first();

        Boolean vent = Boolean.valueOf(ventilation);
        //añadir usuario (el id)
        SPACEMEETING ob = new SPACEMEETING(n, schedule,location,  vent);
        ob.UserRenter = user;
        ob.save();

        renderText("0");
    }

    public static void listaEspaciosPropiosAndroid(long id){
        USER user = USER.find("byId", id).first();

        List<SPACEMEETING> spaceses = user.spaces;

        List<SpaceMeetingDTO> spaceDTOs = new ArrayList<>();
        for (SPACEMEETING space : spaceses) {
            spaceDTOs.add(new SpaceMeetingDTO(space));
        }
        renderJSON(spaceDTOs);
    }

    public static void listaEspaciosAlquiladosAndroid(long id){
        USER user = USER.find("byId", id).first();

        List<MEETING> meetings = user.meetings;

        List<SpaceMeetingDTO> spaceDTOs = new ArrayList<>();
        for (MEETING meeting : meetings) {
            spaceDTOs.add(new SpaceMeetingDTO(meeting.SpaceReserve));
        }
        renderJSON(spaceDTOs);
    }

    public static void getUsuario(long id){
        USER c = USER.find("byId", id).first();
        System.out.printf("Peticion llegada");
        ArrayList<String> user = new ArrayList<>();
        user.add(c.Name);
        user.add(String.format("%d", c.numberTimesRent));
        user.add(String.format("%d", c.numberTimesTenant));
        renderJSON(user);
    }

    public static void rentSpace(long id, int numberPeople, String scheduleAvailable, String location, String ventilation) {
        USER c = USER.find("byId", id).first();
        Boolean vent = Boolean.valueOf(ventilation);
        SPACEMEETING space = SPACEMEETING.find("byNumberpeopleAndScheduleAvailableAndLocationAndVentilation", numberPeople, scheduleAvailable, location, vent).first();

        MEETING meeting = new MEETING(space.numberPeople, space.scheduleAvailable);
        meeting.SpaceReserve = space;
        meeting.UserReserve = c;
        meeting.save();

        renderText("0");
    }

    public static void cancelRentedSpace(long id, int numberPeople, String scheduleAvailable, String location, String ventilation){
        USER c = USER.find("byId", id).first();
        Boolean vent = Boolean.valueOf(ventilation);
        SPACEMEETING spacemeeting = SPACEMEETING.find("byNumberpeopleAndScheduleAvailableAndLocationAndVentilation", numberPeople, scheduleAvailable, location, vent).first();

        MEETING meeting = MEETING.find("byUserreserve_idAndSpacereserve_id", c, spacemeeting).first();
        meeting.delete();

        renderText("0");
    }

    public static void deleteSpace(long id, int numberPeople, String scheduleAvailable, String location, String ventilation){
        USER c = USER.find("byId", id).first();
        Boolean vent = Boolean.valueOf(ventilation);
        SPACEMEETING space = SPACEMEETING.find("byNumberpeopleAndScheduleAvailableAndLocationAndVentilationAndUserrenter_id", numberPeople, scheduleAvailable, location, vent, c).first();

        MEETING meeting = MEETING.find("bySpacereserve_id", space).first();


        meeting.delete();
        space.delete();

        renderText("0");
    }
}