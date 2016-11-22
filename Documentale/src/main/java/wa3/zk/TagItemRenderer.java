package wa3.zk;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ItemRenderer;

import wa3.model.Tag;

public class TagItemRenderer implements ItemRenderer<Tag> {

	@Override
	public String render(Component comp, Tag tag, int index) throws Exception {
		if (tag==null)
			return "";
		return tag.getTag();
	}

}
