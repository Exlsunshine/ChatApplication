// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package z.org.xbill.DNS;

import java.util.*;

/**
 * Recource Record Signature - An RRSIG provides the digital signature of an
 * RRset, so that the data can be authenticated by a DNSSEC-capable resolver.
 * The signature is generated by a key contained in a DNSKEY Record.
 * @see RRset
 * @see DNSSEC
 * @see KEYRecord
 *
 * @author Brian Wellington
 */

public class RRSIGRecord extends SIGBase {

private static final long serialVersionUID = -2609150673537226317L;

RRSIGRecord() {}

Record
getObject() {
	return new RRSIGRecord();
}

/**
 * Creates an RRSIG Record from the given data
 * @param covered The RRset type covered by this signature
 * @param alg The cryptographic algorithm of the key that generated the
 * signature
 * @param origttl The original TTL of the RRset
 * @param expire The time at which the signature expires
 * @param timeSigned The time at which this signature was generated
 * @param footprint The footprint/key id of the signing key.
 * @param signer The owner of the signing key
 * @param signature Binary data representing the signature
 */
public
RRSIGRecord(Name name, int dclass, long ttl, int covered, int alg, long origttl,
	    Date expire, Date timeSigned, int footprint, Name signer,
	    byte [] signature)
{
	super(name, Type.RRSIG, dclass, ttl, covered, alg, origttl, expire,
	      timeSigned, footprint, signer, signature);
}

}
