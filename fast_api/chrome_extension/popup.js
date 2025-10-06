const input = document.getElementById('input');
const style = document.getElementById('style');
const context = document.getElementById('context');
const result = document.getElementById('result');

async function rewrite() {
  const body = {
    originalText: input.value,
    style: style.value,
    context: context.value
  };
  result.textContent = 'Loading...';
  try {
    const res = await fetch('http://localhost:8080/api/rewrite', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    const data = await res.json();
    result.textContent = data.enhancedText || data.message || 'No response';
  } catch (e) {
    result.textContent = 'Error: ' + e.message;
  }
}

document.getElementById('rewrite').addEventListener('click', rewrite);
document.getElementById('copy').addEventListener('click', async () => {
  await navigator.clipboard.writeText(result.textContent || '');
});