const name = 'world';

export function hello(world: string = name): string {
  return `Hello ${world}! `;
}
