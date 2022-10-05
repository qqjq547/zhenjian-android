package com.tiocloud.chat.feature.curr.modify.model;

/**
 * author : TaoWang
 * date : 2020/4/1
 * desc :
 */
public class PageUiModel {
    public int max_word;
    public int min_line;
    public int max_line;
    public String menu_name;
    public String page_title;
    public boolean enableNullContent;
    public CharSequence hint;

    private PageUiModel() {
    }

    public static PageUiModel getInstance(ModifyType type) {
        PageUiModel model = null;
        if (type == ModifyType.GROUP_NAME) {
            model = new PageUiModel();
            model.max_word = 30;
            model.min_line = 1;
            model.max_line = 5;
            model.menu_name = "提交";
            model.page_title = "群名称";
            model.enableNullContent = false;
        } else if (type == ModifyType.GROUP_INTRO) {
            model = new PageUiModel();
            model.max_word = 500;
            model.min_line = 10;
            model.max_line = 20;
            model.menu_name = "保存";
            model.page_title = "群简介";
            model.enableNullContent = true;
        } else if (type == ModifyType.GROUP_NOTICE) {
            model = new PageUiModel();
            model.max_word = 500;
            model.min_line = 10;
            model.max_line = 20;
            model.menu_name = "保存";
            model.page_title = "群公告";
            model.enableNullContent = true;
        } else if (type == ModifyType.GROUP_NICK) {
            model = new PageUiModel();
            model.max_word = 30;
            model.min_line = 1;
            model.max_line = 5;
            model.menu_name = "保存";
            model.page_title = "我的群昵称";
            model.enableNullContent = true;
        } else if (type == ModifyType.CURR_NICK) {
            model = new PageUiModel();
            model.max_word = 30;
            model.min_line = 1;
            model.max_line = 5;
            model.menu_name = "保存";
            model.page_title = "昵称";
            model.enableNullContent = false;
        } else if (type == ModifyType.CURR_SIGN) {
            model = new PageUiModel();
            model.max_word = 50;
            model.min_line = 5;
            model.max_line = 10;
            model.menu_name = "保存";
            model.page_title = "个性签名";
            model.enableNullContent = true;
        } else if (type == ModifyType.USER_REMARK_NAME) {
            model = new PageUiModel();
            model.max_word = 30;
            model.min_line = 1;
            model.max_line = 5;
            model.menu_name = "保存";
            model.page_title = "备注名";
            model.enableNullContent = true;
        } else if (type == ModifyType.ADVISE) {
            model = new PageUiModel();
            model.max_word = 500;
            model.min_line = 10;
            model.max_line = 20;
            model.menu_name = "提交";
            model.page_title = "意见反馈";
            model.enableNullContent = false;
            model.hint = "请输入你要反馈的问题或建议";
        }
        return model;
    }

}
