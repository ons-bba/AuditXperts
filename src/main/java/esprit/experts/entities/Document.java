package esprit.experts.entities;

import java.time.LocalDate;


   import java.time.LocalDate;

    public class Document {
        private int id;
        private String title;
        private String author;
        private LocalDate date;
        private String content;

        public Document(int id, String title, String author, LocalDate date, String content) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.date = date;
            this.content = content;
        }

        // Constructeurs, getters et setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        @Override
        public String toString() {
            return title + " by " + author;
        }
    }
