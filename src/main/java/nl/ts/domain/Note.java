package nl.ts.domain;

public class Note {
    protected Integer id;

    String changeDate;
    String date;
    String name;
    String text;

    public Note(){
    }

    public Note(String changeDate, String date, String name, String text){
        this.changeDate = changeDate;
        this.date = date;
        this.name = name;
        this.text = text;
    }
    static Note[] notes = {
            new Note("20150130", "20150531","Astrid","Vanavond werken"),
            new Note("20150230", "20150531","Maaike","Trainen")
    };

    public static Note[] getSample(){
        return notes;
    }

    @Override
    public String toString() {
        return "{" +
                "\"date\":\"" + date + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"text\":\"" + text + '\"' +
                '}';
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

