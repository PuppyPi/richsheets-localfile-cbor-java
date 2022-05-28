package rebound.richsheets.impls.localfile.formats.gds.cbor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import rebound.dataformats.cbor.CBORUtilities;
import rebound.exceptions.BinarySyntaxException;
import rebound.exceptions.GenericDataStructuresFormatException;
import rebound.richsheets.api.model.RichsheetsTable;
import rebound.richsheets.api.operation.RichsheetsUnencodableFormatException;
import rebound.richsheets.impls.helpers.gds.RichsheetsGDSTranscoder;
import rebound.richsheets.impls.localfile.RichsheetsLocalFileFormatTranscoder;

public enum RichsheetsLocalFileFormatTranscoderForCBOR
implements RichsheetsLocalFileFormatTranscoder
{
	I;
	
	
	@Override
	public RichsheetsTable read(InputStream in) throws IOException, BinarySyntaxException, RichsheetsUnencodableFormatException
	{
		Object gds = CBORUtilities.parseCBOR(in);
		
		try
		{
			return RichsheetsGDSTranscoder.decode(gds);
		}
		catch (GenericDataStructuresFormatException exc)
		{
			throw BinarySyntaxException.inst(exc);
		}
	}
	
	
	@Override
	public void write(RichsheetsTable data, Set<Integer> columnsToAutoresize, OutputStream out) throws IOException
	{
		Object gds = RichsheetsGDSTranscoder.encode(data, columnsToAutoresize);
		CBORUtilities.serializeCBORToStream(gds, out);
	}
	
	
	
	@Override
	public boolean isCapableOfAutoresizingColumns()
	{
		return true;  //Default (null) column width in GDS form is to be interpreted as "autosize" :3
	}
}
