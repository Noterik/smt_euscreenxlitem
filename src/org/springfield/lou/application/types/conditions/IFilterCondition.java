package org.springfield.lou.application.types.conditions;

import org.springfield.lou.fs.FsNode;

public interface IFilterCondition {

	public boolean allow(FsNode node);

}
