package com.jmu.onlinecourse.utils.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.jmu.onlinecourse.activity.MainActivity;
import com.jmu.onlinecourse.entity.Problem;
import com.jmu.onlinecourse.entity.TeachPlan;
import com.jmu.onlinecourse.entity.VideoInfo;
import com.jmu.onlinecourse.utils.DataProviderUtils;
import com.jmu.onlinecourse.utils.PlansDataProviderUtil;
import com.jmu.onlinecourse.utils.database.ProblemInsertUtil;

import java.util.List;

/**
 * @author zjw
 * @date 2021/1/17 0:14
 * @ClassName DatabaseHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * user表创建SQL语句
     */
    private static final String CREATE_VIDEO_TABLE =
            "CREATE TABLE video (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "video_name TEXT NOT NULL, " +
                    "summary TEXT, " +
                    "image_url TEXT, " +
                    "likes INTEGER DEFAULT 0, " +
                    "collection INTEGER DEFAULT 0, " +
                    "play_volume INTEGER DEFAULT 0, " +
                    "detail_url TEXT)";

    /**
     * user表创建SQL语句
     */
    private static final String CREATE_USER_TABLE = "CREATE TABLE user (" +
            "acount text PRIMARY KEY, " +
            "password text NOT NULL, " +
            "username text NOT NULL)";

    /**
     * Collection表创建SQL语句
     */
    private static final String CREATE_COLLECTION_TABLE = "CREATE TABLE collection " +
            "(ID INTEGER, " +
            "name text NOT NULL, " +
            "type text NOT NULL)";

    /**
     * 教学计划表创建语句
     */
    private static final String CREATE_TEACH_PLANS_TABLE =
            "CREATE TABLE teach_plans (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "begin_year INTEGER," +
                    "term INTEGER," +
                    "date TEXT,"+
                    "teaching_content TEXT," +
                    "class_hour DOUBLE)";

    private static final String CREATE_FEEDBACK_LOGS_TABLE =
            "CREATE TABLE feedback_logs (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date TEXT," +
                    "content TEXT)";

    private Context context;
    public static final String PROBLEM = "CREATE TABLE problem(id integer primary key autoincrement," +
            "  title text," +
            "  option_a text," +
            "  option_b text," +
            "  option_c text," +
            "  option_d text," +
            "  examination integer," +
            "  answer text)";
    public static final String SCORE = "CREATE TABLE score(id integer primary key autoincrement," +
            "  score integer)";
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COLLECTION_TABLE);
        db.execSQL(CREATE_VIDEO_TABLE);
        for(VideoInfo videoInfo: DataProviderUtils.getAllVideoInfo()) {
            ContentValues initialValues = new ContentValues();
            initialValues.put("video_name", videoInfo.getVideoName());
            initialValues.put("summary", videoInfo.getSummary());
            initialValues.put("image_url", videoInfo.getImageUrl());
            initialValues.put("detail_url", videoInfo.getDetailUrl());
            db.insert("video", null, initialValues);
        }
        // 创建教学计划数据
        db.execSQL(CREATE_TEACH_PLANS_TABLE);
        for(TeachPlan tp: PlansDataProviderUtil.teachPlans_data) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("begin_year", tp.getBegin_year());
            contentValues.put("term", tp.getTerm());
            contentValues.put("date", tp.getDate());
            contentValues.put("teaching_content", tp.getTeaching_content());
            contentValues.put("class_hour", tp.getClass_hour());
            db.insert("teach_plans", null, contentValues);
        }
        db.execSQL(CREATE_FEEDBACK_LOGS_TABLE);
        //创建problem表sql语句
        db.execSQL(PROBLEM);
        //从txt文件获取数据
        List<Problem> problems = ProblemInsertUtil.getDataFromTxt(context);
        //将数据插入到数据库
        ProblemInsertUtil.insertDataIntoProblem(db, problems);
        db.execSQL(SCORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
