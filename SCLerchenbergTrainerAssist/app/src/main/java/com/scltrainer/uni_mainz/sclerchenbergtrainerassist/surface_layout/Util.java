package com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.util.Log;

import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.DBConnection;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.DBHelper;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.DBInfo;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.R;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Background;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.Layout;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.MaterialComponent;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.MaterialType;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.PathComponent;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.PathType;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.QuadVBO;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TextureFactory;
import com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.opengl.TriangleVBO;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.FieldType.SOCCER_FIELD;
import static com.scltrainer.uni_mainz.sclerchenbergtrainerassist.surface_layout.component.SurfaceType.SOCCER_GRASS;

/**
 * A utility class storing constants and providing static utility methods used several times.
 */
public class Util {

    public static Vector2f pixelDensity;
    public static final float LINE_WIDTH_INCH = 0.03f;
    public static final float ARROW_SIZE_INCH = 0.12f;
    public static final float SCALE_SELECTED = 0.7f;
    public static final float MIN_SCALE_SELECTED = 1.1f;
    public static final float ALPHA_SELECTED = 0.5f;
    public static final float REMOVE_LAYER_SCALE_NDC = 0.1f;
    public static final float REMOVE_LAYER_GREY = 0.5f;
    public static final Vector4f REMOVE_COLOR = new Vector4f(1,0f,0f,1);
    public static final float MAX_ZOOM = 8.0f;
    public static final float PATH_ALPHA = 0.65f;
    public static final float TOUCH_SIZE_INCH = 0.2f;
    public static final float POINT_SIZE_INCH = 0.06f;

    private Util() {
    }

    public static void init(Activity activity) {
        pixelDensity = getPixelDensity(activity);
    }

    private static Vector2f getPixelDensity(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new Vector2f(metrics.xdpi, metrics.ydpi);
    }


    private static Cursor selectCursorLayout(DBConnection dbConnection, int entryID){
        String s = DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " = ? ";
        String[] sArgs = {"" + entryID};
        Log.i("LayoutFragment", "entryID in cursor: " + entryID);
        String[] sArr = {DBInfo.EXERCISE_COLUMN_NAME_GRAPHIC};
        return dbConnection.select(DBInfo.EXERCISE_TABLE_NAME, sArr, s, sArgs);
    }

    /**
     * Forces the gl thread to reload its resources.
     * This is required each time the glcontext  is lost, due to pausing or destroying the GLsurface.
     */
    public static void forceReloadResources() {
        TextureFactory.reset();
        QuadVBO.reset();
        TriangleVBO.reset();
    }

    /**
     * The default layout is used for testing.
     * It only contains the lines of a soccer field and a grass background.
     */
    public static Layout defaultLayout() {
        // for testing
        Layout layout = new Layout();
        layout.background = new Background(SOCCER_GRASS, SOCCER_FIELD);
        return layout;
    }

    /**
     * Loads a layout form the local database.
     */
    public static Layout loadLayoutFromDB(int entryID, Context context) {
        DBConnection dbConnection = DBHelper.getConnection(context);
        Cursor dbCursor = selectCursorLayout(dbConnection, entryID);
        dbCursor.moveToNext();
        String jsonString = dbCursor.getString(0);
        try {
            JSONObject json = new JSONObject(jsonString);
            return Layout.fromJSON(json);
        } catch (JSONException e) {
            Log.e("JSON", e.getMessage());
            return defaultLayout();
        }
    }

    /**
     * Saves a layout to the local database.
     */
    public static boolean saveLayoutToDB(int entryID, Layout layout, Context context) {
        try {
            JSONObject jsonLayout = layout.toJSON();
            DBConnection dbConnection = DBHelper.getConnection(context);

            ContentValues graphic = new ContentValues();
            graphic.put(DBInfo.EXERCISE_COLUMN_NAME_GRAPHIC, jsonLayout.toString());
            String s = DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " = ? ";
            String[] sArgs = {"" + entryID};
            dbConnection.update(DBInfo.EXERCISE_TABLE_NAME, graphic, s, sArgs);
            return true;
        } catch (JSONException e) {
            Log.e("JSON", e.getMessage());
            return false;
        }
    }

    /**
     * Pre-loads the textures of materials to speed up the creation process.
     */
    public static void loadDrawables(Context context){
        for (MaterialType mat : MaterialType.values()) {
            TextureFactory.loadTexture(context, mat.getTextureResID());
        }
    }

}
