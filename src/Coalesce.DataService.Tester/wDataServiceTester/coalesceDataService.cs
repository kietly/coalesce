﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.18034
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------



[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ServiceModel.ServiceContractAttribute(Namespace="http://services.irems.proto.com/", ConfigurationName="ICoalesceDataService")]
public interface ICoalesceDataService
{
    
    // CODEGEN: Parameter 'return' requires additional schema information that cannot be captured using the parameter mode. The specific attribute is 'System.Xml.Serialization.XmlElementAttribute'.
    [System.ServiceModel.OperationContractAttribute(Action="http://services.irems.proto.com/ICoalesceDataService/getEntityRequest", ReplyAction="http://services.irems.proto.com/ICoalesceDataService/getEntityResponse")]
    [System.ServiceModel.XmlSerializerFormatAttribute()]
    [return: System.ServiceModel.MessageParameterAttribute(Name="return")]
    getEntityResponse getEntity(getEntityRequest request);
    
    // CODEGEN: Parameter 'return' requires additional schema information that cannot be captured using the parameter mode. The specific attribute is 'System.Xml.Serialization.XmlElementAttribute'.
    [System.ServiceModel.OperationContractAttribute(Action="http://services.irems.proto.com/ICoalesceDataService/getEntityKeysRequest", ReplyAction="http://services.irems.proto.com/ICoalesceDataService/getEntityKeysResponse")]
    [System.ServiceModel.XmlSerializerFormatAttribute()]
    [return: System.ServiceModel.MessageParameterAttribute(Name="return")]
    getEntityKeysResponse getEntityKeys(getEntityKeysRequest request);
    
    // CODEGEN: Parameter 'return' requires additional schema information that cannot be captured using the parameter mode. The specific attribute is 'System.Xml.Serialization.XmlElementAttribute'.
    [System.ServiceModel.OperationContractAttribute(Action="http://services.irems.proto.com/ICoalesceDataService/getFieldValueRequest", ReplyAction="http://services.irems.proto.com/ICoalesceDataService/getFieldValueResponse")]
    [System.ServiceModel.XmlSerializerFormatAttribute()]
    [return: System.ServiceModel.MessageParameterAttribute(Name="return")]
    getFieldValueResponse getFieldValue(getFieldValueRequest request);
    
    // CODEGEN: Parameter 'return' requires additional schema information that cannot be captured using the parameter mode. The specific attribute is 'System.Xml.Serialization.XmlElementAttribute'.
    [System.ServiceModel.OperationContractAttribute(Action="http://services.irems.proto.com/ICoalesceDataService/getEntityByNameRequest", ReplyAction="http://services.irems.proto.com/ICoalesceDataService/getEntityByNameResponse")]
    [System.ServiceModel.XmlSerializerFormatAttribute()]
    [return: System.ServiceModel.MessageParameterAttribute(Name="return")]
    getEntityByNameResponse getEntityByName(getEntityByNameRequest request);
    
    // CODEGEN: Parameter 'return' requires additional schema information that cannot be captured using the parameter mode. The specific attribute is 'System.Xml.Serialization.XmlElementAttribute'.
    [System.ServiceModel.OperationContractAttribute(Action="http://services.irems.proto.com/ICoalesceDataService/getEntityXMLKeysRequest", ReplyAction="http://services.irems.proto.com/ICoalesceDataService/getEntityXMLKeysResponse")]
    [System.ServiceModel.XmlSerializerFormatAttribute()]
    [return: System.ServiceModel.MessageParameterAttribute(Name="return")]
    getEntityXMLKeysResponse getEntityXMLKeys(getEntityXMLKeysRequest request);
    
    // CODEGEN: Parameter 'return' requires additional schema information that cannot be captured using the parameter mode. The specific attribute is 'System.Xml.Serialization.XmlElementAttribute'.
    [System.ServiceModel.OperationContractAttribute(Action="http://services.irems.proto.com/ICoalesceDataService/getEntityXMLByNameRequest", ReplyAction="http://services.irems.proto.com/ICoalesceDataService/getEntityXMLByNameResponse")]
    [System.ServiceModel.XmlSerializerFormatAttribute()]
    [return: System.ServiceModel.MessageParameterAttribute(Name="return")]
    getEntityXMLByNameResponse getEntityXMLByName(getEntityXMLByNameRequest request);
    
    // CODEGEN: Parameter 'return' requires additional schema information that cannot be captured using the parameter mode. The specific attribute is 'System.Xml.Serialization.XmlElementAttribute'.
    [System.ServiceModel.OperationContractAttribute(Action="http://services.irems.proto.com/ICoalesceDataService/setEntityRequest", ReplyAction="http://services.irems.proto.com/ICoalesceDataService/setEntityResponse")]
    [System.ServiceModel.XmlSerializerFormatAttribute()]
    [return: System.ServiceModel.MessageParameterAttribute(Name="return")]
    setEntityResponse setEntity(setEntityRequest request);
    
    // CODEGEN: Parameter 'return' requires additional schema information that cannot be captured using the parameter mode. The specific attribute is 'System.Xml.Serialization.XmlElementAttribute'.
    [System.ServiceModel.OperationContractAttribute(Action="http://services.irems.proto.com/ICoalesceDataService/getEntityXMLRequest", ReplyAction="http://services.irems.proto.com/ICoalesceDataService/getEntityXMLResponse")]
    [System.ServiceModel.XmlSerializerFormatAttribute()]
    [return: System.ServiceModel.MessageParameterAttribute(Name="return")]
    getEntityXMLResponse getEntityXML(getEntityXMLRequest request);
    
    // CODEGEN: Parameter 'return' requires additional schema information that cannot be captured using the parameter mode. The specific attribute is 'System.Xml.Serialization.XmlElementAttribute'.
    [System.ServiceModel.OperationContractAttribute(Action="http://services.irems.proto.com/ICoalesceDataService/getXPathRequest", ReplyAction="http://services.irems.proto.com/ICoalesceDataService/getXPathResponse")]
    [System.ServiceModel.XmlSerializerFormatAttribute()]
    [return: System.ServiceModel.MessageParameterAttribute(Name="return")]
    getXPathResponse getXPath(getXPathRequest request);
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntity", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityRequest
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg0;
    
    public getEntityRequest()
    {
    }
    
    public getEntityRequest(string arg0)
    {
        this.arg0 = arg0;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityResponse", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityResponse
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string @return;
    
    public getEntityResponse()
    {
    }
    
    public getEntityResponse(string @return)
    {
        this.@return = @return;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityKeys", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityKeysRequest
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg0;
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=1)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg1;
    
    public getEntityKeysRequest()
    {
    }
    
    public getEntityKeysRequest(string arg0, string arg1)
    {
        this.arg0 = arg0;
        this.arg1 = arg1;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityKeysResponse", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityKeysResponse
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute("return", Form=System.Xml.Schema.XmlSchemaForm.Unqualified, IsNullable=true)]
    public string[] @return;
    
    public getEntityKeysResponse()
    {
    }
    
    public getEntityKeysResponse(string[] @return)
    {
        this.@return = @return;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getFieldValue", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getFieldValueRequest
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg0;
    
    public getFieldValueRequest()
    {
    }
    
    public getFieldValueRequest(string arg0)
    {
        this.arg0 = arg0;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getFieldValueResponse", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getFieldValueResponse
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string @return;
    
    public getFieldValueResponse()
    {
    }
    
    public getFieldValueResponse(string @return)
    {
        this.@return = @return;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityByName", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityByNameRequest
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg0;
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=1)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg1;
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=2)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg2;
    
    public getEntityByNameRequest()
    {
    }
    
    public getEntityByNameRequest(string arg0, string arg1, string arg2)
    {
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityByNameResponse", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityByNameResponse
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string @return;
    
    public getEntityByNameResponse()
    {
    }
    
    public getEntityByNameResponse(string @return)
    {
        this.@return = @return;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityXMLKeys", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityXMLKeysRequest
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg0;
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=1)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg1;
    
    public getEntityXMLKeysRequest()
    {
    }
    
    public getEntityXMLKeysRequest(string arg0, string arg1)
    {
        this.arg0 = arg0;
        this.arg1 = arg1;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityXMLKeysResponse", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityXMLKeysResponse
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute("return", Form=System.Xml.Schema.XmlSchemaForm.Unqualified, IsNullable=true)]
    public string[] @return;
    
    public getEntityXMLKeysResponse()
    {
    }
    
    public getEntityXMLKeysResponse(string[] @return)
    {
        this.@return = @return;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityXMLByName", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityXMLByNameRequest
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg0;
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=1)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg1;
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=2)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg2;
    
    public getEntityXMLByNameRequest()
    {
    }
    
    public getEntityXMLByNameRequest(string arg0, string arg1, string arg2)
    {
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityXMLByNameResponse", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityXMLByNameResponse
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string @return;
    
    public getEntityXMLByNameResponse()
    {
    }
    
    public getEntityXMLByNameResponse(string @return)
    {
        this.@return = @return;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="setEntity", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class setEntityRequest
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg0;
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=1)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg1;
    
    public setEntityRequest()
    {
    }
    
    public setEntityRequest(string arg0, string arg1)
    {
        this.arg0 = arg0;
        this.arg1 = arg1;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="setEntityResponse", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class setEntityResponse
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public bool @return;
    
    public setEntityResponse()
    {
    }
    
    public setEntityResponse(bool @return)
    {
        this.@return = @return;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityXML", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityXMLRequest
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg0;
    
    public getEntityXMLRequest()
    {
    }
    
    public getEntityXMLRequest(string arg0)
    {
        this.arg0 = arg0;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getEntityXMLResponse", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getEntityXMLResponse
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string @return;
    
    public getEntityXMLResponse()
    {
    }
    
    public getEntityXMLResponse(string @return)
    {
        this.@return = @return;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getXPath", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getXPathRequest
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg0;
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=1)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg1;
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=2)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string arg2;
    
    public getXPathRequest()
    {
    }
    
    public getXPathRequest(string arg0, string arg1, string arg2)
    {
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
[System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
[System.ServiceModel.MessageContractAttribute(WrapperName="getXPathResponse", WrapperNamespace="http://services.irems.proto.com/", IsWrapped=true)]
public partial class getXPathResponse
{
    
    [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://services.irems.proto.com/", Order=0)]
    [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
    public string @return;
    
    public getXPathResponse()
    {
    }
    
    public getXPathResponse(string @return)
    {
        this.@return = @return;
    }
}

[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
public interface ICoalesceDataServiceChannel : ICoalesceDataService, System.ServiceModel.IClientChannel
{
}

[System.Diagnostics.DebuggerStepThroughAttribute()]
[System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
public partial class CoalesceDataServiceClient : System.ServiceModel.ClientBase<ICoalesceDataService>, ICoalesceDataService
{
    
    public CoalesceDataServiceClient()
    {
    }
    
    public CoalesceDataServiceClient(string endpointConfigurationName) : 
            base(endpointConfigurationName)
    {
    }
    
    public CoalesceDataServiceClient(string endpointConfigurationName, string remoteAddress) : 
            base(endpointConfigurationName, remoteAddress)
    {
    }
    
    public CoalesceDataServiceClient(string endpointConfigurationName, System.ServiceModel.EndpointAddress remoteAddress) : 
            base(endpointConfigurationName, remoteAddress)
    {
    }
    
    public CoalesceDataServiceClient(System.ServiceModel.Channels.Binding binding, System.ServiceModel.EndpointAddress remoteAddress) : 
            base(binding, remoteAddress)
    {
    }
    
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    getEntityResponse ICoalesceDataService.getEntity(getEntityRequest request)
    {
        return base.Channel.getEntity(request);
    }
    
    public string getEntity(string arg0)
    {
        getEntityRequest inValue = new getEntityRequest();
        inValue.arg0 = arg0;
        getEntityResponse retVal = ((ICoalesceDataService)(this)).getEntity(inValue);
        return retVal.@return;
    }
    
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    getEntityKeysResponse ICoalesceDataService.getEntityKeys(getEntityKeysRequest request)
    {
        return base.Channel.getEntityKeys(request);
    }
    
    public string[] getEntityKeys(string arg0, string arg1)
    {
        getEntityKeysRequest inValue = new getEntityKeysRequest();
        inValue.arg0 = arg0;
        inValue.arg1 = arg1;
        getEntityKeysResponse retVal = ((ICoalesceDataService)(this)).getEntityKeys(inValue);
        return retVal.@return;
    }
    
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    getFieldValueResponse ICoalesceDataService.getFieldValue(getFieldValueRequest request)
    {
        return base.Channel.getFieldValue(request);
    }
    
    public string getFieldValue(string arg0)
    {
        getFieldValueRequest inValue = new getFieldValueRequest();
        inValue.arg0 = arg0;
        getFieldValueResponse retVal = ((ICoalesceDataService)(this)).getFieldValue(inValue);
        return retVal.@return;
    }
    
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    getEntityByNameResponse ICoalesceDataService.getEntityByName(getEntityByNameRequest request)
    {
        return base.Channel.getEntityByName(request);
    }
    
    public string getEntityByName(string arg0, string arg1, string arg2)
    {
        getEntityByNameRequest inValue = new getEntityByNameRequest();
        inValue.arg0 = arg0;
        inValue.arg1 = arg1;
        inValue.arg2 = arg2;
        getEntityByNameResponse retVal = ((ICoalesceDataService)(this)).getEntityByName(inValue);
        return retVal.@return;
    }
    
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    getEntityXMLKeysResponse ICoalesceDataService.getEntityXMLKeys(getEntityXMLKeysRequest request)
    {
        return base.Channel.getEntityXMLKeys(request);
    }
    
    public string[] getEntityXMLKeys(string arg0, string arg1)
    {
        getEntityXMLKeysRequest inValue = new getEntityXMLKeysRequest();
        inValue.arg0 = arg0;
        inValue.arg1 = arg1;
        getEntityXMLKeysResponse retVal = ((ICoalesceDataService)(this)).getEntityXMLKeys(inValue);
        return retVal.@return;
    }
    
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    getEntityXMLByNameResponse ICoalesceDataService.getEntityXMLByName(getEntityXMLByNameRequest request)
    {
        return base.Channel.getEntityXMLByName(request);
    }
    
    public string getEntityXMLByName(string arg0, string arg1, string arg2)
    {
        getEntityXMLByNameRequest inValue = new getEntityXMLByNameRequest();
        inValue.arg0 = arg0;
        inValue.arg1 = arg1;
        inValue.arg2 = arg2;
        getEntityXMLByNameResponse retVal = ((ICoalesceDataService)(this)).getEntityXMLByName(inValue);
        return retVal.@return;
    }
    
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    setEntityResponse ICoalesceDataService.setEntity(setEntityRequest request)
    {
        return base.Channel.setEntity(request);
    }
    
    public bool setEntity(string arg0, string arg1)
    {
        setEntityRequest inValue = new setEntityRequest();
        inValue.arg0 = arg0;
        inValue.arg1 = arg1;
        setEntityResponse retVal = ((ICoalesceDataService)(this)).setEntity(inValue);
        return retVal.@return;
    }
    
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    getEntityXMLResponse ICoalesceDataService.getEntityXML(getEntityXMLRequest request)
    {
        return base.Channel.getEntityXML(request);
    }
    
    public string getEntityXML(string arg0)
    {
        getEntityXMLRequest inValue = new getEntityXMLRequest();
        inValue.arg0 = arg0;
        getEntityXMLResponse retVal = ((ICoalesceDataService)(this)).getEntityXML(inValue);
        return retVal.@return;
    }
    
    [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)]
    getXPathResponse ICoalesceDataService.getXPath(getXPathRequest request)
    {
        return base.Channel.getXPath(request);
    }
    
    public string getXPath(string arg0, string arg1, string arg2)
    {
        getXPathRequest inValue = new getXPathRequest();
        inValue.arg0 = arg0;
        inValue.arg1 = arg1;
        inValue.arg2 = arg2;
        getXPathResponse retVal = ((ICoalesceDataService)(this)).getXPath(inValue);
        return retVal.@return;
    }
}
