package com.example.final_535_app.utils

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import com.permissionx.guolindev.PermissionMediator
import com.permissionx.guolindev.request.ExplainScope

object PermissionUtils {
    /**
     * 高级版
     */
    open fun bestPermissionX(request: PermissionMediator, permissions:List<String>, context: Context) {
        request.permissions(
                permissions
            )
            .setDialogTintColor(Color.parseColor("#008577"), Color.parseColor("#83e8dd"))
            .onExplainRequestReason { scope: ExplainScope, deniedList: List<String?>? ->
                val message = "PermissionX需要您同意以下权限才能正常使用"
                scope.showRequestReasonDialog(deniedList as List<String>, message, "确定", "取消")
            }
            .request { allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?> ->
                if (allGranted) {
                    Toast.makeText(context, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }
    }
}