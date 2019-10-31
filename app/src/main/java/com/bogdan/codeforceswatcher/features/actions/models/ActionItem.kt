package com.bogdan.codeforceswatcher.features.actions.models

import android.text.SpannableStringBuilder
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.users.models.colorTextByUserRank
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

sealed class ActionItem {

    class Action(action: CFAction) : ActionItem() {

        var commentatorHandle: CharSequence
        var title: String
        var content: String
        var commentatorAvatar: String
        var creationTimeSeconds: Long

        init {
            val comment = action.comment ?: throw NullPointerException()
            commentatorAvatar = (getRightAvatarLink(comment.commentatorAvatar))
            commentatorHandle = formatCommentatorHandle(
                comment.commentatorHandle, comment.commentatorRank
            )
            title = action.blogEntry.title
            creationTimeSeconds = comment.creationTimeSeconds
            content = comment.text
        }

        private fun getRightAvatarLink(avatarLink: String) =
            if (avatarLink.startsWith("https:")) {
                avatarLink
            } else {
                "https:$avatarLink"
            }

        private fun formatCommentatorHandle(handle: String, rank: String?): CharSequence {
            val colorHandle = colorTextByUserRank(handle, rank)
            val commentedByString = CwApp.app.getString(R.string.commented_by)
            val handlePosition = commentedByString.indexOf("%1\$s")

            return SpannableStringBuilder(commentedByString)
                .replace(handlePosition, handlePosition + "%1\$s".length, colorHandle)
        }
    }

    object Stub : ActionItem()
}