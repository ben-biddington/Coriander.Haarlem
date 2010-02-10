<%@include file="../include-internal.jsp"%>
<div class="dilbert">
    <p class="title"><c:out value="${results.title}" escapeXml="true" /></p>
    <div class="item">
        <img src="${results.media.url}" />
    </div>
</div>
