package com.tiocloud.chat.feature.session.group.fragment.ait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/7/24
 *     desc   : @ 联系人数据
 * </pre>
 */
public class AitContactsModel {

    // 已@ 的成员
    private final Map<String, AitBlock> aitBlocks = new HashMap<>();

    /**
     * 查所有被@的群成员
     */
    public List<String> queryAitTeamMember() {
        List<String> teamMembers = new ArrayList<>();
        for (String account : aitBlocks.keySet()) {
            AitBlock block = aitBlocks.get(account);
            if (block.aitType == AitContactType.TEAM_MEMBER && block.valid()) {
                teamMembers.add(account);
            }
        }
        return teamMembers;
    }

    /**
     * 根据账号查询 @块
     *
     * @param account 账号
     */
    public AitBlock queryAitBlock(String account) {
        return aitBlocks.get(account);
    }

    /**
     * 查第一个被@的机器人
     */
    public String queryFirstAitRobot() {
        int start = -1;
        String robotAccount = null;

        for (String account : aitBlocks.keySet()) {
            AitBlock block = aitBlocks.get(account);
            if (block.valid() && block.aitType == AitContactType.ROBOT) {
                int blockStart = block.queryFirstSegmentStart();
                if (blockStart == -1) {
                    continue;
                }
                if (start == -1 || blockStart < start) {
                    start = blockStart;
                    robotAccount = account;
                }
            }
        }
        return robotAccount;
    }

    /**
     * 根据结束位置查找segment
     *
     * @param end 结束位置
     */
    public AitBlock.AitSegment queryAitSegmentByEndPos(int end) {
        for (String account : aitBlocks.keySet()) {
            AitBlock block = aitBlocks.get(account);
            AitBlock.AitSegment segment = block.queryLastSegmentByEnd(end);
            if (segment != null) {
                return segment;
            }
        }
        return null;
    }

    /**
     * 插入 @块
     *
     * @param account 账号
     * @param name    名称
     * @param type    类型
     * @param start   起始位置
     */
    public void insertAitMember(String account, String name, int type, int start) {
        AitBlock aitBlock = aitBlocks.get(account);
        if (aitBlock == null) {
            aitBlock = new AitBlock(name, type);
            aitBlocks.put(account, aitBlock);
        }
        aitBlock.insertSegment(start);
    }

    /**
     * 清除所有的@块
     */
    public void clear() {
        aitBlocks.clear();
    }

    /**
     * 文本插入后更新 @块 的起止位置
     *
     * @param start      起始位置
     * @param changeText 文字
     */
    public void onInsertText(int start, String changeText) {
        Iterator<String> iterator = aitBlocks.keySet().iterator();
        while (iterator.hasNext()) {
            String account = iterator.next();
            AitBlock block = aitBlocks.get(account);
            block.updateSegment_moveRight(start, changeText);
            if (!block.valid()) {
                iterator.remove();
            }
        }
    }

    /**
     * 文本删除后更新 @块 的起止位置
     *
     * @param start  起始位置
     * @param length 长度
     */
    public void onDeleteText(int start, int length) {
        Iterator<String> iterator = aitBlocks.keySet().iterator();
        while (iterator.hasNext()) {
            String account = iterator.next();
            AitBlock block = aitBlocks.get(account);
            block.updateSegment_moveLeft(start, length);
            if (!block.valid()) {
                iterator.remove();
            }
        }
    }
}
