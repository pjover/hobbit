{{- define "labels" }}
  labels:
    date: {{ now | htmlDate }}
    release: {{ .Release.Name }}
    chart: {{ .Chart.Name }}
    version: {{ .Chart.Version }}
{{- end }}
