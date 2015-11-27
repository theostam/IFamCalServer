package nl.ts.controller;

import nl.ts.service.AuthenticationService;
import nl.ts.service.NotesDBService;
import nl.ts.domain.Note;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/")
public class NotesController implements Serializable {

@GET
@Path("notes/lastmodificationdate")
@Produces( MediaType.APPLICATION_JSON )
    public String getLastModificationDate(@QueryParam("user") String user, @QueryParam("callback") String callback) {
        System.out.println("request /notes/lastmodificationdate: "+ "user:"+ user+ " callback:"+callback);
        if (!AuthenticationService.authenticate(user, null)) return null;
        NotesDBService repository = new NotesDBService();
        String lcd = repository.findLastChangeDate(user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d = new Date();
        return createJSONresult( callback, lcd );
    }


    @GET
    @Path("notes")
    @Produces( MediaType.APPLICATION_JSON )
        public String getNotesSince( @QueryParam("user") String user, @QueryParam("date") String date, @QueryParam("callback") String callback) {
        System.out.println("request /notes/since: "+ "user:"+ user+ " callback:"+callback + " date:"+date);
        if (!AuthenticationService.authenticate(user, null)) return null;

        NotesDBService repository = new NotesDBService();
        List<Note> notes = repository.getNotesSince(user,date);
        String s = stringifyNoteList(notes);
//        String s = stringifyNoteArray( Note.getSample() );
        return createJSONresult(callback, s);
    }
    @GET
    @Path("/notes/saveorupdate/{date}/{name}/{text}/{callback}")
    @Produces( MediaType.APPLICATION_JSON )
    public String saveOrUpdate(@PathParam("date") String date, @PathParam("name") String user, @PathParam("text") String text, @PathParam("callback")String callback) {
        System.out.println("request /notes/saveorupdate: "); //+ "user:"+ user+ " text: "+ text + " callback: nop");
        if (!AuthenticationService.authenticate(user, null)) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d = new Date();
        Note note = new Note(sdf.format(new Date()), date, user, text);

        NotesDBService repository = new NotesDBService();
        repository.saveOrUpdate( note );
        return createJSONresult(callback, "'OKIEDOKIE'");
    }


    private String stringifyNoteList(List<Note> notes){
        if (notes.size() == 0) return null;

        String result = "[" + notes.get(0).toString();
        for( int i=1; i < notes.size(); i++){
            result += "," + notes.get(i).toString();
        }
        return result + "]";
    }

    private String stringifyNoteArray(Note[] arr){
        String result = "[" + arr[0].toString();
        for( int i=1; i < arr.length; i++){
            result += "," + arr[i].toString();
        }
        return result + "]";
    }

    private String createJSONresult(String callback, String result){

//        return callback+" ({'result':" + result + "});";
        return "{\"result\":" + result + "}";
    }
}


