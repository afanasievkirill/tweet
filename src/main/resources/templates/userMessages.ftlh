<#import "parts/common.ftl" as c>
<@c.page>
    <!-- message edit -->
    <#if isCurrentUser>
        <#include "parts/messageEdit.ftl"/>
    </#if>
    <!-- message list -->
    <#include "parts/messageList.ftl"/>
</@c.page>