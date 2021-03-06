//*********************************************************
//
//    Copyright (c) Microsoft. All rights reserved.
//    This code is licensed under the Apache License Version 2.0.
//    THIS CODE IS PROVIDED *AS IS* WITHOUT WARRANTY OF
//    ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING ANY
//    IMPLIED WARRANTIES OF FITNESS FOR A PARTICULAR
//    PURPOSE, MERCHANTABILITY, OR NON-INFRINGEMENT.
//
//*********************************************************

package com.microsoft.uprove;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import com.microsoft.uprove.FieldZq.ZqElement;

/**
 * Parameters indicating the manner in which an {@link IssuerKeyAndParameters}
 * instance is to be generated.
 * <p>
 * <code>IssuerKeyAndParameters</code> objects are generated by configuring an
 * instance of this class and then invoking the instance's
 * {@link #generate() generate} method. The parameters that affect
 * <code>IssuerKeyAndParameters</code> generation are:
 * </p>
 *
 * <table border="1" cellpadding="5" summary="Issuer key and parameters generation parameters">
 * <tr>
 * <th>Parameter</th>
 * <th>Default Value</th>
 * <th>Description</th>
 * </tr>
 *
 * <tr>
 * <td>Issuer Parameters UID</td>
 * <td><code>null</code></td>
 * <td>The unique identifier of the Issuer parameters. This parameter MUST be set
 * via the {@link #setParametersUID(byte[]) setParametersUID} method.
 * The SDK does not interpret Issuer parameters UID values.</td>
 * </tr>
 * 
 * <tr>
 * <td>Prime Order Group</td>
 * <td><code>null</code></td>
 * <td>An instance of the mathematical group in which all
 * cryptographic operations will take place. If none is specified, the
 * default group generated by
 * {@link
 * com.microsoft.uprove.DefaultSubgroupFactory}
 * will be used.</td>
 * </tr>
 * 
 * <tr>
 * <td>Hash Algorithm</td>
 * <td><code>null</code></td>
 * <td>The name of the <code>MessageDigest</code> algorithm to be used for
 * cryptographic operations. If none is specified, the SHA variant matching
 * the prime order group size is used.</td>
 * </tr>
 *
 * <tr>
 * <td>Encoding Bytes</td>
 * <td><code>null</code></td>
 * <td>The array of encoding bytes specifying how the corresponding attributes will be
 * encoded in wallets issued using the generated issuer parameters. The length of this
 * parameter defines the number of token attributes supported by the issuer parameters.
 * This parameter MUST be set via the {@link #setEncodingBytes(byte[]) setEncodingBytes} method.</td>
 * </tr>
 * 
 * <tr>
 * <td>Specification</td>
 * <td><code>null</code></td>
 * <td>The application-specific specification field. This parameter MUST be set
 * via the {@link #setSpecification(byte[]) setSpecification} method.
 * The SDK does not interpret Specification values.</td>
 * </tr>
 */
public final class IssuerSetupParameters {

	// the parameters UID
    private byte[] parametersUID;

    // the prime order group
    private PrimeOrderGroup group;
    
    // the hash algorithm UID
    private String hashAlgorithmUID;

    // the hash booleans
    private byte[] encodingBytes;
    
    // the specification
    private byte[] specification;

    // support Device boolean
    private boolean supportDevice = false;
    
	/**
     * Sets the Issuer parameters UID.
     * 
     * @param parametersUID an Issuer parameters UID.
     */
    public void setParametersUID(byte[] parametersUID) {
		this.parametersUID = parametersUID;
	}

    /**
     * Returns the Issuer parameters UID. 
     * @return an Issuer parameters UID. 
     */
	public byte[] getParametersUID() {
		return parametersUID;
	}

	/**
	 * Sets the Issuer parameters prime order group.
	 * @param group a prime order group.
	 */
	public void setGroup(PrimeOrderGroup group) {
		this.group = group;
	}

	/**
	 * Returns the Issuer parameters prime order group.
	 * @return a prime order group.
	 */
	public PrimeOrderGroup getGroup() {
		return group;
	}

	/**
	 * Sets the Issuer parameters hash algorithm UID. 
	 * @param hashAlgorithmUID a hash algorithm UID.
	 */
	public void setHashAlgorithmUID(String hashAlgorithmUID) {
		this.hashAlgorithmUID = hashAlgorithmUID;
	}

	/**
	 * Gets the Issuer parameters hash algorithm UID.
	 * @return a hash algorithm UID.
	 */
	public String getHashAlgorithmUID() {
		return hashAlgorithmUID;
	}

	/**
	 * Sets the Issuer parameters encoding bytes.
	 * @param encodingBytes an encoding bytes array.
	 */
	public void setEncodingBytes(byte[] encodingBytes) {
		this.encodingBytes = encodingBytes;
	}

	/**
	 * Gets the Issuer parameters encoding bytes. 
	 * @return an encoding bytes array.
	 */
	public byte[] getEncodingBytes() {
		return encodingBytes;
	}

	/**
	 * Sets the Issuer parameters specification.
	 * @param specification a specification.
	 */
	public void setSpecification(byte[] specification) {
		this.specification = specification;
	}

	/**
	 * Gets the Issuer parameters specification. 
	 * @return a specification.
	 */
	public byte[] getSpecification() {
		return specification;
	}

	/**
	 * Sets the support Device boolean.
	 * @param supportDevice true to support Device, false otherwise.
	 */
	public void setSupportDevice(boolean supportDevice) {
		this.supportDevice = supportDevice;
	}
	
	/**
	 * Gets the support Device boolean.
	 * @return the support Device boolean.
	 */
	public boolean getSupportDevice() {
		return supportDevice;
	}
	
	private int getDefaultGroupSize() {
		if (hashAlgorithmUID == "SHA-1") { // FIXME: remove support for SHA-1?
			return 160;
		} else if (hashAlgorithmUID == "SHA-256") {
			return 256;
		} else if (hashAlgorithmUID == "SHA-512") {
			return 512;
		} 
		
		// our default
		return 256;
	}
	
	/**
     * Validates <code>this</code> parameters instance. Specifically, the
     * following tests are made:
     * <ul>
     * <li>Does {@link #getParametersUID() getParametersUID} return a
     * non-<code>null</code> identifier?</li>
     * <li>If {@link #getGroup() getGroup} returns
     * non-<code>null</code>, is the group valid?</li>
     * <li>If {@link #getHashAlgorithmUID() getHashAlgorithmUID} returns
     * non-<code>null</code>, can the hash algorithm be created?</li> 
     * <li>Does {@link #getEncodingBytes() getEncodingBytes} return a
     * non-<code>null</code> array?</li>
     * <li>Does {@link #getSpecification() getSpecification} return a
     * non-<code>null</code> value?</li>
     * </ul>
     * @throws IllegalStateException if <code>this</code> is not in a
     * suitable state for generating an Issuer.
     * @throws NoSuchProviderException if the configured
     * {@link java.security.MessageDigest} provider is not installed.
     * @throws NoSuchAlgorithmException if the desired
     * <code>MessageDigest</code> algorithm cannot be found.
     * @see com.microsoft.uprove.Config#getMessageDigestProvider()
     */
    public void validate() throws NoSuchProviderException,
            NoSuchAlgorithmException {
    	if (this.parametersUID == null) {
    		throw new IllegalStateException("Issuer parameters UID is not set.");
    	}
    	
    	if (this.hashAlgorithmUID != null) {
    		// can we get a message digest for the specified algorithm?
    	    ConfigImpl.getMessageDigest(this.hashAlgorithmUID);
    	}
    	
    	if (this.group != null) {
    		this.group.validate();
    	}
    	
    	if (this.encodingBytes == null) {
    		throw new IllegalStateException("Encoding bytes parameter is not set.");	
    	}

    }

    /**
     * Creates an Issuer key and Parameters object according to
     * specified generation parameters.
     * @return an Issuer's private key and parameters, generated as
     * specified by <code>this</code> parameter instance.
     * Ownership of the referent is given to the caller.
     * @throws IllegalStateException if <code>params</code> is not in a
     * suitable state for generating an Issuer, as indicated by the
     * {@link #validate() validate} method.
     * @throws NoSuchProviderException if the configured
     * {@link java.security.MessageDigest} provider is not installed.
     * @throws NoSuchAlgorithmException if the desired
     * <code>MessageDigest</code> algorithm cannot be found.
     * @see com.microsoft.uprove.Config#getMessageDigestProvider()
     * @see #validate()
     */
    public IssuerKeyAndParameters generate() throws NoSuchProviderException,
            NoSuchAlgorithmException {
    	// make sure the parameters are valid
    	validate();
    	
    	if (this.group == null) {
    		this.group = DefaultSubgroupFactory.getDefaultSubroup(getDefaultGroupSize());
    	}
    	
    	int size = this.encodingBytes.length + 2 + (supportDevice ? 1 : 0);
    	GroupElement g = this.group.getGenerator();
    	ZqElement[] privateKey = group.getZq().getRandomElements(size, false); 
    	GroupElement[] publicKey = new GroupElement[size];
    	GroupElement[] proverIssuanceValues = new GroupElement[size];
    	ZqElement y0 = privateKey[0];
    	for (int i=0; i<size; i++) {
    		publicKey[i] = g.exponentiate(privateKey[i]);
    		proverIssuanceValues[i] = publicKey[i].exponentiate(y0);
    	}
    	
    	IssuerParameters ip = new IssuerParameters();
    	ip.setEncodingBytes(this.encodingBytes);
    	ip.setGroup(this.group);
    	ip.setHashAlgorithmUID(this.hashAlgorithmUID);
    	ip.setParametersUID(this.parametersUID);
		ip.setProverIssuanceValues(ProtocolHelper.getEncodedArray(proverIssuanceValues));
		ip.setPublicKey(ProtocolHelper.getEncodedArray(publicKey));
    	ip.setSpecification(this.specification);
    	return new IssuerKeyAndParameters(ip, privateKey[0].toByteArray());
    }
}
