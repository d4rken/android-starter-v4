---
layout: plain
permalink: /changelog
title: "Changelog"
---

# Changelog

{% for release in site.github.releases %}

## {{ release.tag_name }} - {{ release.published_at | date: "%B %d, %Y" }}

{% assign clean_body = release.body | strip %}
{% assign no_comments = clean_body | replace: "<!-- Release notes generated using configuration in .github/release.yml", "" %}
{% assign no_comments = no_comments | split: "-->" %}
{% if no_comments.size > 1 %}
  {% assign clean_content = no_comments[1] | strip %}
{% else %}
  {% assign clean_content = no_comments[0] | strip %}
{% endif %}

{% comment %} Check if there are any bullet points (actual release notes) {% endcomment %}
{% if clean_content contains "## " or clean_content contains "- " %}
  {% assign spaced_content = clean_content | replace: "
- ", "

- " %}
  {{ spaced_content | markdownify }}
{% else %}
  *No release notes available.*
  
  {{ clean_content | markdownify }}
{% endif %}

---
{% endfor %}