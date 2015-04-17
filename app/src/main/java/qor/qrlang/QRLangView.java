package qor.qrlang;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class QRLangView extends SurfaceView  implements SurfaceHolder.Callback, JSInterface{


    private int[][][] letters = {{{0,1,0},{0,1,0},{1,0,1},{1,1,1},{1,0,1}},{{1,1,1,0},{1,0,0,1},{1,1,1,0},{1,0,0,1},{1,1,1,0}},{{0,1,1,0},{1,0,0,1},{1,0,0,0},{1,0,0,1},{0,1,1,0}},{{1,1,1,0},{1,0,0,1},{1,0,0,1},{1,0,0,1},{1,1,1,0}},{{1,1,1},{1,0,0},{1,1,1},{1,0,0},{1,1,1}},{{1,1,1},{1,0,0},{1,1,1},{1,0,0},{1,0,0}},{{0,1,1,1,1},{1,0,0,0,0},{1,0,0,1,1},{1,0,0,0,1},{0,1,1,1,1}},{{1,0,0,1},{1,0,0,1},{1,1,1,1},{1,0,0,1},{1,0,0,1}},{{1},{1},{1},{1},{1}},{{0,0,1},{0,0,1},{0,0,1},{1,0,1},{1,1,0}},{{1,0,0,1},{1,0,1,0},{1,1,0,0},{1,0,1,0},{1,0,0,1}},{{1,0,0},{1,0,0},{1,0,0},{1,0,0},{1,1,1}},{{1,0,0,0,1},{1,0,0,0,1},{1,1,0,1,1},{1,1,0,1,1},{1,0,1,0,1}},{{1,0,0,1},{1,1,0,1},{1,1,0,1},{1,0,1,1},{1,0,0,1}},{{0,1,1,1,0},{1,0,0,0,1},{1,0,0,0,1},{1,0,0,0,1},{0,1,1,1,0}},{{1,1,1},{1,0,1},{1,1,1},{1,0,0},{1,0,0}},{{0,1,1,1,0},{1,0,0,0,1},{1,0,1,0,1},{1,0,0,1,0},{0,1,1,0,1}},{{1,1,1,1},{1,0,0,1},{1,1,1,1},{1,0,1,0},{1,0,0,1}},{{0,1,1},{1,0,0},{1,1,1},{0,0,1},{1,1,0}},{{1,1,1},{0,1,0},{0,1,0},{0,1,0},{0,1,0}},{{1,0,0,1},{1,0,0,1},{1,0,0,1},{1,0,0,1},{0,1,1,0}},{{1,0,1},{1,0,1},{1,0,1},{0,1,0},{0,1,0}},{{1,0,1,0,1},{1,0,1,0,1},{1,0,1,0,1},{1,0,1,0,1},{0,1,0,1,0}},{{1,0,1},{0,1,0},{0,1,0},{0,1,0},{1,0,1}},{{1,0,1},{0,1,0},{0,1,0},{0,1,0},{0,1,0}},{{1,1,1},{0,0,1},{0,1,0},{1,0,0},{1,1,1}},{{0,1,0},{1,1,0},{0,1,0},{0,1,0},{0,1,0}},{{1,1,1},{0,0,1},{0,1,0},{1,0,0},{1,1,1}},{{1,1,1},{0,0,1},{0,1,0},{0,0,1},{1,1,1}},{{0,0,1},{0,1,1},{1,0,1},{1,1,1},{0,0,1}},{{1,1,1},{1,0,0},{1,1,1},{0,0,1},{1,1,1}},{{0,1,1},{1,0,0},{1,1,1},{1,0,1},{1,1,1}},{{1,1,1},{0,0,1},{0,1,0},{0,1,0},{0,1,0}},{{1,1,1},{1,0,1},{0,1,0},{1,0,1},{1,1,1}},{{1,1,1},{1,0,1},{1,1,1},{0,0,1},{1,1,0}},{{1,1,1},{1,0,1},{1,0,1},{1,0,1},{1,1,1}},{{0,0},{0,0},{0,0},{0,0},{0,0}}};
    private String letterSeq = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ";

    private MotionEvent event;
    private String code;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint = new Paint();
    public JSInterpreter interpreter;

    private Context context;

    /**
     * Constructor
     */
    public QRLangView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        //So we can listen for events...
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
    }

    //Starts the view
    public void start(){
        //Decompresses the code to javascript
        Decompressor decompressor = new Decompressor();
        code = decompressor.decompress(code);

        //Adds extra code to allow it to run in a loop.
        code = Decompressor.baseVariables + code;
        code = code + ";setInterval(function(){update();Android.lock();draw();Android.unlock();Android.update();}, 33)";

        //Creates a new interpreter
        interpreter = new JSInterpreter(context, this, "Android");

        //Starts the interpreter
        interpreter.interpret(code);
    }

    public void setCode(String code){
        this.code = code;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    //Implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceCreated(SurfaceHolder holder) { }

    //Implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }

    /**
     * JSInterface method
     * Locks and clears the canvas
     */
    @Override
    public void lock() {
        canvas = holder.lockCanvas();
        canvas.drawRGB(0,0,0);
    }

    /**
     * JSInterface method
     * unlocks the canvas
     */
    @Override
    public void unlock() {
        holder.unlockCanvasAndPost(canvas);
    }

    /**
     * JSInterface method
     * Logs the given message for debugging
     */
    @Override
    public void log(String value) {
        Log.d("QRLang",value);
    }

    /**
     * JSInterface method
     * draws a box in a given position
     */
    @Override
    public void drawbox(float x, float y, float w, float h) {
        int X = (int)x * 8;
        int Y = (int)y * 8;
        int W = (int)w * 8;
        int H = (int)h * 8;
        canvas.drawRect(new Rect(X,Y,X+W,Y+H), paint);

    }

    /**
     * JSInterface method
     * draws a box in a given position with the set colour
     */
    @Override
    public void drawbox(float x, float y, float w, float h, int col) {
        setColour(col);
        drawbox(x,y,w,h);
    }

    /**
     * JSInterface method
     * retrieves whether or not a key is presses
     */
    @Override
    public boolean key(String k) {
        if(event == null) return false;

        if(k.equals("up")) return event.getY() < 360;
        if(k.equals("down")) return event.getY() > 360;
        if(k.equals("left")) return event.getX() < 640;
        if(k.equals("right")) return event.getX() > 640;

        return false;
    }

    /**
     * JSInterface method
     * updates touchevents
     */
    @Override
    public void update(){
        event = null;
    }

    /**
     * JSInterface method
     * writes a message in a given position
     */
    @Override
    public void write(String text, float x, float y) {
        int pos = 0;
        for(int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            int[][] letter = letters[letterSeq.indexOf(c)];

            for(int j = 0; j < 5; j++){
                for(int k = 0; k < letter[0].length; k++){
                    if(letter[j][k] == 1)drawbox(x + k + pos, y + j, 1, 1);
                }
            }
            pos += letter[0].length + 1;
        }
    }

    /**
     * JSInterface method
     * writes a message in a given position with a given colour
     */
    @Override
    public void write(String text, float x, float y, int col) {
        setColour(col);
        write(text, x, y);
    }

    /**
     * Sets the colour with a 0-7 input
     */
    private void setColour(int col){
        paint.setARGB(255, getColour(col,4),getColour(col,2), getColour(col,1));
    }

    private int getColour(int col, int i){
        if((col & i) == i) return 255;
        return 0;
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.event = event;
        return true;
    }
}