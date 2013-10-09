{extends 'pwe.tpl'}

{block name="breadcrumbs" append}
    {PWE->getCurrentModuleInstance assign="module"}
{* TODO: implement it in PWE *}
    {if $module|is_a:'BreadcrumbsGenerator'}
        {$module->generateBreadcrumbs() assign=bcrumbs}

        {foreach $bcrumbs as $item}
            {if $item.selected}
                &gt;
                <b><a class="hl" href="{$item.$a.link}">{$item.$a.title}</a></b>
            {else}
                &gt;
                <b><a href="{$item.$a.link}">{$item.$a.title}</a></b>
            {/if}
        {/foreach}
    {/if}
{/block}


{block name="head" append}
    {if $smarty.server.SERVER_ADDR==$smarty.server.REMOTE_ADDR}
        <style type='text/css'>
            {include file=$smarty.current_dir|cat:'/../../img/plugins.css'}
        </style>
    {else}
        <link rel="stylesheet" href="{$IMG_HREF}/plugins.css"/>
    {/if}
{/block}

{block name="header_title"}
    <span style="font-size: 2em;">jmeter-plugins.org</span>
    <br/>
    <span style="font-size: 0.8em;">Every load test needs some sexy features!</span>
{/block}

{block name="header_right"}
        <table style="white-space: nowrap; vertical-align: middle;">
            <tr>
                <td style="padding: 0 0.5em;">
                    <a href="http://blazemeter.com/?utm_source=jmplinnerpages&utm_medium=cpc&utm_content=jmpininnerpgs&utm_campaign=JMeter%2BPlug%2BIn%2BWiki"
                       target="_blank"><img src="/img/site/bz_small.jpg" alt=""></a>
                </td>
                <td style="padding: 0 0.5em;">
                    <a href="http://ubikloadpack.com/?utm_source=jpgc&utm_medium=cpc&utm_campaign=sponsorship"
                       target="_blank"><img src="/img/site/ubik.png" alt=""></a>
                </td>
                <td style="padding: 0 0.5em;">
                    <div style="background-color: #ffffd6; border: 1px solid #EE9900; height: 56px; font-size: 80%;
padding: 2px 4px; vertical-align: middle; cursor: pointer;" onclick="window.location.href='http://loadosophia.org/?utm_source=jpgc&utm_medium=header&utm_campaign=sponsorship';">
                <a href="http://loadosophia.org/?utm_source=jpgc&utm_medium=header&utm_campaign=sponsorship" style="text-decoration: none; color: black;">
                        <span style="color: gray">Sponsored by:</span><br/>
                        <span style="font-size: 120%; font-weight: bold; color: #cc6600;">Loadosophia.org &ndash;</span><br/>
                        load test results<br/> analysis service
                    </a>
                    </div>
                </td>
            </tr>
        </table>
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

{block name="footer_center"}
<td style="font-size: 80%;">
<span style="color: gray;">Licensed under <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache 2.0 License</a></span>
</td>
{/block}