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

{% comment %} Make Full Changelog links clickable in ALL cases {% endcomment %}
{% assign lines = clean_content | split: "
" %}
{% assign processed_lines = "" %}
{% for line in lines %}
  {% if line contains "**Full Changelog**:" %}
    {% assign parts = line | split: ": " %}
    {% if parts.size > 1 %}
      {% assign url = parts[1] | strip %}
      {% assign clickable_line = "**[Full Changelog](" | append: url | append: ")" %}
      {% assign processed_lines = processed_lines | append: clickable_line | append: "
" %}
    {% else %}
      {% assign processed_lines = processed_lines | append: line | append: "
" %}
    {% endif %}
  {% else %}
    {% assign processed_lines = processed_lines | append: line | append: "
" %}
  {% endif %}
{% endfor %}

{% comment %} Check if there are any bullet points (actual release notes) {% endcomment %}
{% if processed_lines contains "## " or processed_lines contains "- " %}
  {% assign spaced_content = processed_lines | replace: "
- ", "

- " %}
  {{ spaced_content | markdownify }}
{% else %}
  *No release notes available.*
  
  {{ processed_lines | markdownify }}
{% endif %}

---
{% endfor %}