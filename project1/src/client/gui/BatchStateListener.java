package client.gui;

import java.util.ArrayList;

import client.gui.indexerWindow.tableEntry.TableEntryCell;

public interface BatchStateListener {

	public void downloadBatch();

	public void selectField();
	
	public void updateData();
	
	public void updateSelected(TableEntryCell selectedCell);

	public void performLogout(String function);

	public void updateImage(String function);

}
