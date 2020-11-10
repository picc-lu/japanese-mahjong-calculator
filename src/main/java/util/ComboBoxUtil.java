package util;

import org.apache.commons.beanutils.ConvertUtils;

import javax.swing.*;

/**
 * JComboBox 工具类
 *
 * @author PICC
 * @date 20/11/10 16:25
 * @since JDK 1.8
 */
public class ComboBoxUtil {
    /**
     * 获取当前下拉列表中选中的值
     *
     * @param jComboBox 下拉列表
     * @param <T>       泛型
     * @return 选中的值
     * @author PICC
     */
    public static <T, E> E getSelected(JComboBox<T> jComboBox, Class<E> clazz) {
        return (E) ConvertUtils.convert(jComboBox.getItemAt(jComboBox.getSelectedIndex()), clazz);
    }
}
