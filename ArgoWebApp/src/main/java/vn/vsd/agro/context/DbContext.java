package vn.vsd.agro.context;

public interface DbContext
{
	public IContext<?> getDbContext(IContext<String> context, IClient<?> client);
}
