package org.springfield.lou.application.types.conditions;

import org.springfield.fs.FsNode;

public interface IFilterCondition {

	public boolean allow(FsNode node);

}
