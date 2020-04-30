package vn.vsd.agro.domain;

import java.io.Serializable;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.embed.StatusEmbed;

public interface INeedApprove<K extends Serializable> {
	
	public static final String COLUMNNAME_APPROVE_STATUS = "status";
	public static final String COLUMNNAME_APPROVE_STATUS_VALUE = COLUMNNAME_APPROVE_STATUS + ".status";
	
    public static final int APPROVE_STATUS_PENDING = 0;
    public static final int APPROVE_STATUS_APPROVED = 1;
    public static final int APPROVE_STATUS_REJECTED = -1;
    public static final int APPROVE_STATUS_IN_PROGRESS = 2;
    
    public void init(IContext<K> context);
    
    public void processIt(IContext<K> context, String message);
    
    public void approveIt(IContext<K> context, String message);
    
    public void rejectIt(IContext<K> context, String message);
    
    public StatusEmbed getStatus();
    
	public boolean isPending();
	
	public boolean isProcessing();
	
	public boolean isApproved();
	
	public boolean isRejected();
}
