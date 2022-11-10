import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


object LocalCacheUtils {

    /**
     * 本地缓存
     */
    fun getBitmapFromLocal(context: Context?, url: Int?): Bitmap? {
        try {
            val FILE_PATH = context?.externalCacheDir?.absolutePath+"/cache"
            val fileName: String = url.toString()
            val file = File(FILE_PATH, fileName)
            if (file.exists()) {
                return BitmapFactory.decodeStream(FileInputStream(file))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 往本地sd卡写入图片
     */
    fun setBitmap2Local(context: Context?, url: Int?, bitmap: Bitmap?) {
        try {
            val FILE_PATH = context?.externalCacheDir?.absolutePath+"/cache"
            val fileName: String = url.toString()
            val file = File(FILE_PATH, fileName)
            val fileParent = file.getParentFile();
            if (!fileParent!!.exists()) {
                fileParent.mkdirs();
            }
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file));

        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
}