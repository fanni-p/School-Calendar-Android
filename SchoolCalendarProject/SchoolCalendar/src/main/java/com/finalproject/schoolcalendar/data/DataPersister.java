package com.finalproject.schoolcalendar.data;

import com.finalproject.schoolcalendar.models.HomeworkModel;
import com.finalproject.schoolcalendar.models.LessonModel;
import com.finalproject.schoolcalendar.models.SubjectModel;
import com.finalproject.schoolcalendar.models.UserModel;
import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by Fani on 11/13/13.
 */
public class DataPersister {
    private static final String BASE_URL = "http://timetableservices.apphb.com/api/";

    private static Gson mGson = new Gson();

    public static HttpResponseHelper Login(UserModel userModel) {
        String loginUrl = BASE_URL + "auth/token";

        String userModelToString = mGson.toJson(userModel);
        HttpResponseHelper responseResult = HttpRequest.post(loginUrl, userModelToString, null);

        return responseResult;
    }

    public static HttpResponseHelper Register(UserModel userModel) {
        String registerUrl = BASE_URL + "user/register";

        String userModelToString = mGson.toJson(userModel);
        HttpResponseHelper responseResult = HttpRequest.post(registerUrl, userModelToString, null);

        return responseResult;
    }

    public static HttpResponseHelper Logout(String accessToken) {
        String logoutUrl = BASE_URL + "user/logout";
        HttpResponseHelper responseResult = HttpRequest.put(logoutUrl, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper GetLessonsPerDay(String accessToken) {
        Date date = new Date();
        String currentDay = android.text.format.DateFormat.format("EEEE", date).toString();
        String lessonsPerDayUrl = BASE_URL + "lesson/byDay/" + currentDay;

        HttpResponseHelper responseResult = HttpRequest.get(lessonsPerDayUrl, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper GetLessonsPerDay(String day, String accessToken) {
        Date date = new Date();
        String currentDay = android.text.format.DateFormat.format("EEEE", date).toString();
        String lessonsPerDayUrl = BASE_URL + "lesson/byDay/" + day;

        HttpResponseHelper responseResult = HttpRequest.get(lessonsPerDayUrl, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper AddNewLesson(LessonModel lessonModel, String accessToken) {
        String addNewLessonUrl = BASE_URL + "lesson";

        String subjectModelToString = mGson.toJson(lessonModel);
        HttpResponseHelper responseResult = HttpRequest.post(addNewLessonUrl, subjectModelToString, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper GetAllSubjects(String accessToken) {
        String allSubjectsUrl = BASE_URL + "subject";

        HttpResponseHelper responseResult = HttpRequest.get(allSubjectsUrl, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper AddNewSubject(SubjectModel subjectModel, String accessToken) {
        String addNewSubjectUrl = BASE_URL + "subject";

        String subjectModelToString = mGson.toJson(subjectModel);
        HttpResponseHelper responseResult = HttpRequest.post(addNewSubjectUrl, subjectModelToString, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper EditSubject(SubjectModel subjectModel, String accessToken, int id) {
        String editSubjectUrl = BASE_URL + "subject/" + id;

        String subjectModelToString = mGson.toJson(subjectModel);
        HttpResponseHelper responseResult = HttpRequest.post(editSubjectUrl, subjectModelToString, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper DeleteSubject(String accessToken, int id) {
        String deleteSubjectUrl = BASE_URL + "subject/" + id;

        HttpResponseHelper responseResult = HttpRequest.delete(deleteSubjectUrl, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper GetAllHomework(String accessToken) {
        String allHomeworkUrl = BASE_URL + "homework";

        HttpResponseHelper responseResult = HttpRequest.get(allHomeworkUrl, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper AddNewHomework(HomeworkModel homeworkModel, String accessToken) {
        String addNewHomeworkUrl = BASE_URL + "homework";

        String subjectModelToString = mGson.toJson(homeworkModel);
        HttpResponseHelper responseResult = HttpRequest.post(addNewHomeworkUrl, subjectModelToString, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper MarkHomeworkAsDone(String accessToken, int id) {
        String deleteHomeworkUrl = BASE_URL + "homework/" + id;

        HttpResponseHelper responseResult = HttpRequest.put(deleteHomeworkUrl, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper EditHomework(SubjectModel subjectModel, String accessToken, int id) {
        String editHomeworkUrl = BASE_URL + "homework/" + id;

        String homeworkModelToString = mGson.toJson(subjectModel);
        HttpResponseHelper responseResult = HttpRequest.post(editHomeworkUrl, homeworkModelToString, accessToken);

        return responseResult;
    }

    public static HttpResponseHelper DeleteHomework(String accessToken, int id) {
        String deleteHomeworkUrl = BASE_URL + "homework/" + id;

        HttpResponseHelper responseResult = HttpRequest.delete(deleteHomeworkUrl, accessToken);

        return responseResult;
    }
}
