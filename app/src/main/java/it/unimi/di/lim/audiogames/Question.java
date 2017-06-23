package it.unimi.di.lim.audiogames;

class Question {

    private String mQuestion;
    private String mAnswer1;
    private String mAnswer2;
    private String mAnswer3;
    private String mAnswer4;
    private int mCorrect;

    Question(String question, String answer1, String answer2, String answer3, String answer4, int correct) {
        mQuestion = question;
        mAnswer1 = answer1;
        mAnswer2 = answer2;
        mAnswer3 = answer3;
        mAnswer4 = answer4;
        mCorrect = correct;
    }

    String getQuestion() {
        return mQuestion;
    }

    String getAnswer1() {
        return mAnswer1;
    }

    String getAnswer2() {
        return mAnswer2;
    }

    String getAnswer3() {
        return mAnswer3;
    }

    String getAnswer4() {
        return mAnswer4;
    }

    int getCorrect() {
        return mCorrect;
    }
}
