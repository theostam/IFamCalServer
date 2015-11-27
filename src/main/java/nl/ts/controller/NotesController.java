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
    public String getLastModificationDate(@QueryParam("user") String user) {
        System.out.println("request /notes/lastmodificationdate: "+ "user:"+ user);
        if (!AuthenticationService.authenticate(user, null)) return null;
        NotesDBService repository = new NotesDBService();
        String response = createJSONresult( repository.findLastChangeDate(user) );
        System.out.println("response: "+ response);
        return  response;
    }


    @GET
    @Path("notes")
    @Produces( MediaType.APPLICATION_JSON )
        public String getNotesSince( @QueryParam("user") String user, @QueryParam("date") String date) {
        System.out.println("request /notes/since: "+ "user:"+ user + " date:"+date);
        if (!AuthenticationService.authenticate(user, null)) return null;

        NotesDBService repository = new NotesDBService();
        List<Note> notes = repository.getNotesSince(user,date);
//        String s = stringifyNoteArray( Note.getSample() );
        String response = createJSONresult( stringifyNoteList(notes) );
        System.out.println("response: "+ response);
        return  response;
    }
    @GET
    @Path("/notes/saveorupdate/{date}/{name}/{text}")
    @Produces( MediaType.APPLICATION_JSON )
    public String saveOrUpdate(@PathParam("date") String date, @PathParam("name") String user, @PathParam("text") String text) {
        System.out.println("request /notes/saveorupdate: "); //+ "user:"+ user+ " text: "+ text + " callback: nop");
        if (!AuthenticationService.authenticate(user, null)) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date d = new Date();
        Note note = new Note(sdf.format(new Date()), date, user, text);

        NotesDBService repository = new NotesDBService();
        repository.saveOrUpdate( note );
        return createJSONresult( "1");
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

    private String createJSONresult( String result){

//        return callback+" ({'result':" + result + "});";
        return "{\"result\":" + result + "}";
    }
}


