{extends 'pwe.tpl'}

{block name="breadcrumbs" append}
    {PWE->getCurrentModuleInstance assign="module"}
    {* TODO: implement it in PWE *}
    {if $module|is_a:'BreadcrumbsGenerator'}
        {$module->generateBreadcrumbs() assign=bcrumbs}

        {foreach $bcrumbs as $item}
            {if $item.selected}
                &gt; <b><a class="hl" href="{$item.$a.link}">{$item.$a.title}</a></b>
                {else}
                &gt; <b><a href="{$item.$a.link}">{$item.$a.title}</a></b>
                {/if}            
            {/foreach}
        {/if}
    {/block}


{block name="head" append}
    {if $smarty.server.SERVER_ADDR==$smarty.server.REMOTE_ADDR}
        <style type='text/css'>
            {include file=$smarty.current_dir|cat:'/plugins.css'}
        </style>
    {else}
        <link rel="stylesheet" href="{$IMG_HREF}/hlas/hlas.css" />
    {/if}
{/block}

{block name="header_title"}
    <span style="font-size: 2em;">jmeter-plugins.org</span><br/>
    <span style="font-size: 0.8em;">Every load test needs some sexy features!</span>
{/block}

{block name="header_right"}
    <div style='padding: 0em 0.5em;'>
        <a href="http://blazemeter.com/?utm_source=jmplinnerpages&amp;utm_medium=cpc&amp;utm_content=jmpininnerpgs&amp;utm_campaign=JMeter%2BPlug%2BIn%2BWiki" 
           target="_blank"><img src="http://apc.kg/img/jpgc/bz_small.jpg" alt=""></a>
    </div>
{/block}

{block name="title"}
    {PWE->getCurrentModuleInstance assign="module"}
    {if $module|is_a:'TitleGenerator'}
        {$module->generateTitle() assign="title"}
        {$title|default:$node.$i.title}
    {else}
        {$smarty.block.parent}
    {/if}
    :: JMeter-Plugins.org
{/block}

{block name="content" prepend}
    {include file="counter.tpl"}
{/block}