---
layout: default
title: Changelog
---

# Changelog

{% for release in site.github.releases %}

## {{ release.tag_name }} - {{ release.published_at | date: "%B %d, %Y" }}

{% assign formatted_body = release.body | replace: "- ", "

- " %}
  {{ formatted_body | markdownify }}

---
{% endfor %}