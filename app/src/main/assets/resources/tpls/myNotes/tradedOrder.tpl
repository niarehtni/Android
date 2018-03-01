{{!-- 我的票据-已上架 --}}
{{#each data}}
<ul class="tradeRecordItem clearfix cashItem" data-json="{{toJsonString this}}">

    <li class="tabs1" title="{{sourceText}}"><!-- 来源 -->
        {{#if sourceText}}
            {{sourceText}}
        {{else}}
            -
        {{/if}}
    </li>
    <li class="tabs2" title="{{typeText}}"><!-- 票据类别 -->
        {{#if typeText}}
            {{typeText}}
        {{else}}
            -
        {{/if}}
    </li>

    <li class="tabs3" title="{{billAttributeText}}"><!-- 票据属性 -->
        {{#if billAttributeText}}
            {{billAttributeText}}
        {{else}}
            -
        {{/if}}
    </li>


    <li class="tabs4" title="{{billNo}}"><!-- 票号后六位 --> 
        {{#if billNo}}
            {{billNoType billNo}}
        {{else}}
            -
        {{/if}}
    </li>
    <li class="tabs5" title="{{name5}}"><!-- 到期日 --> 
        {{#if dueDate}}
            {{dueDate}}
        {{else}}
            -
        {{/if}}
    </li>
    <li class="tabs6" title="{{yuanConversion faceAmount}}"><!-- 票面金额（元） --> 
        {{#if faceAmount}}
            {{yuanConversion faceAmount}}
        {{else}}
            -
        {{/if}}
    </li>
    <li class="tabs7" title="{{shelvesTime}}"><!-- 上架时间 --> 
        {{#if shelvesTime}}
            {{shelvesTimeType shelvesTime}}
        {{else}}
            -
        {{/if}}
    </li>
    <li class="tabs8" title="{{transferState}}"><!-- 票据状态 --> 
        {{#if transferState}}
            {{transferStateType transferState}}
        {{else}}
            -
        {{/if}}
    </li>

    
    <li class="tabs9">
        <!-- <a class="offTheShelf" href="javascript:;" data-id={{billNo}}>下架</a>
        <a class="modify" href="javascript:;" data-id={{billNo}}>修改</a> -->
        {{lfTransferStateBtn transferState billNo}}
        <a class="lookUp" href="javascript:;" data-id={{billNo}}>查看</a> 
    </li>
</ul>
{{/each}}