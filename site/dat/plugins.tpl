<!DOCTYPE html>
{assign var=i value='!i'}{assign var=a value='!a'}{assign var=p value='!p'}{assign var=c value='!c'}
{URL->getFullCount assign=urlFullCount}
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>
    {PWE->getCurrentModuleInstance assign="module"}
    {if $module|is_a:'PWE\Modules\TitleGenerator'}
      {$module->generateTitle() assign="title"}
      {$title|default:$node.$i.title}
    {else}
      {$node.$i.title|default:$node.$a.link}
    {/if} :: JMeter-Plugins.org
  </title>

<script>
if (window.location.hostname == 'jmeter-plugins.org' && window.location.protocol != "https:") {
    window.location.href = "https:" + window.location.href.substring(window.location.protocol.length);
}
</script>

  <link rel="icon" href="/img/site/logo.png"/>
  <!-- Bootstrap -->

  <!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

  <!-- Optional theme -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->

  <meta name="keywords" content="{$node.$i.keywords|default:$node.$i.keywords}"/>
  <meta name="description" content="{$node.$i.description|default:$node.$i.description}"/>
  <script type='text/javascript' src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  {if $smarty.server.SERVER_ADDR==$smarty.server.REMOTE_ADDR}
    <style type='text/css'>
      {include file='../img/plugins.css'}
    </style>
  {else}
    <link rel="stylesheet" href="/img/plugins.css"/>
  {/if}
  <link rel='stylesheet' type='text/css' href="//maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"/>
</head>

<body>
<header class="header clearfix">
  <div class="container">
    <div class="row topmost">
      <div class="col-md-12">
        {URL->getParamsCount assign=paramsCount}
        {URL->getMatchedCount assign=matchCount}
        {assign var=root value=$node}
        {assign var=path value='../'|str_repeat:$paramsCount}
        {while $root}
          {if $root.$a.link}
            {assign var=path value='../'|cat:$path}
          {/if}

          {capture name=breadcrumbs}
            {if $path != '../' || !$root.$p}
              {if $root.$a.link}
                <span><a class="nohl"
                         href="{$path}{$root.$a.link}/">{$root.$a.title|default:$root.$a.link}</a></span>
              {else}
                <span><a class="nohl" href="{$path}">{$root.$a.title|default:$root.$a.link}</a></span>
              {/if}
              {if $smarty.capture.breadcrumbs|strlen}&gt;{/if}
              {$smarty.capture.breadcrumbs}
            {else}
              <span><a class="hl"
                       href="{$path}{$root.$a.link}/">{$root.$a.title|default:$root.$a.link}</a></span>
              {$smarty.capture.breadcrumbs}
            {/if}
          {/capture}
          {assign var=root value=$root.$p}
        {/while}

        {if $smarty.capture.breadcrumbs}
          <div class="position">
            {$smarty.capture.breadcrumbs}
            {PWE->getCurrentModuleInstance assign="module"}
            {if $module|is_a:'PWE\Modules\BreadcrumbsGenerator'}
              {$module->generateBreadcrumbs() assign=bcrumbs}

              {foreach $bcrumbs as $item}
                {if $item.selected}
                  &gt;
                  <a class="hl" href="{$item.$a.link}">{$item.$a.title}</a>
                {else}
                  &gt;
                  <a href="{$item.$a.link}">{$item.$a.title}</a>
                {/if}
              {/foreach}
            {/if}
          </div>
        {/if}
      </div>
    </div>

    <div class="site-header">
      <div class="logo"><a href="/"><img src="/img/site/logo.png" alt="JMeter-Plugins.org"/></a></div>
      <div class="purpose">
        <div>jmeter-plugins.org</div>
        <div>Every load test needs some sexy features!</div>
      </div>
      <div class="sponsorship pull-right">
        <a href="http://blazemeter.com/?utm_source=jmplinnerpages&utm_medium=cpc&utm_content=jmpininnerpgs&utm_campaign=JMeter%2BPlug%2BIn%2BWiki"
           target="_blank"><img src="/img/site/blazemeter-mini.png" alt=""></a>
      </div>
    </div>

    {PWE->getStructLevel level=1 assign=level1}
    {if $level1}
      <nav class="navbar navbar-default">
        <ul class="nav navbar-nav">
          {math assign=upper_repeats equation='x-1' x=$urlFullCount}
          {foreach $level1 as $item1}
            {if $item1.$a.menu}
              {if $item1.selected}
                <li role="presentation" class="active">
                  <a href="{'../'|str_repeat:$upper_repeats}{$item1.$a.link}/"><i
                            class="fa {$item1.$a.ico}"></i> {$item1.$a.title|default:$item1.$a.link}</a>
                </li>
              {else}
                <li role="presentation">
                  <a href="{'../'|str_repeat:$upper_repeats}{$item1.$a.link}/"><i
                            class="fa {$item1.$a.ico}"></i> {$item1.$a.title|default:$item1.$a.link}</a>
                </li>
              {/if}
            {/if}
          {/foreach}
        </ul>
<div class="pull-right" style="vertical-align: middle; padding-right: 0.75em; padding-top: 0.15em">
<!-- Place this tag where you want the button to render. -->
<a class="github-button" href="https://github.com/undera/jmeter-plugins" data-count-href="/undera/jmeter-plugins/stargazers" data-count-api="/repos/undera/jmeter-plugins#stargazers_count" data-count-aria-label="# stargazers on GitHub" aria-label="Star undera/jmeter-plugins on GitHub">Star</a>
</div>
      </nav>
    {/if}
  </div>

</header>
<!-- /container -->

<div class="container">
  {PWE->getContent}
</div>

<footer>
  <div class="container">
    <span>&copy; 2009-{"Y"|date} <a href="https://www.linkedin.com/in/andreypohilko">Andrey Pokhilko</a> and <a href="/wiki/Contributors/">project contributors</a></span><br/>
    <span>Licensed under <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache 2.0 License</a></span>
  </div>

  <!-- Latest compiled and minified JavaScript -->
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<!-- Place this tag in your head or just before your close body tag. -->
<script async defer src="https://buttons.github.io/buttons.js"></script>
  {include file="dat/counter.tpl"}

</footer>

</body>
</html>

