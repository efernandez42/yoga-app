module.exports = {
  preset: 'jest-preset-angular',
  setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
  testEnvironment: 'jsdom',
  bail: false,
  verbose: false,
  collectCoverage: true,
  coverageDirectory: './coverage',
  coverageReporters: ['html', 'text', 'lcov', 'text-summary'],
  testPathIgnorePatterns: ['<rootDir>/node_modules/', '<rootDir>/cypress/'],
  coveragePathIgnorePatterns: [
    '<rootDir>/node_modules/',
    '<rootDir>/cypress/',
    '<rootDir>/src/environments/',
    '<rootDir>/src/main.ts',
    '<rootDir>/src/polyfills.ts',
    '<rootDir>/src/app/app-routing.module.ts',
    '<rootDir>/src/app/app.module.ts',
    '<rootDir>/src/app/features/auth/auth-routing.module.ts',
    '<rootDir>/src/app/features/auth/auth.module.ts',
    '<rootDir>/src/app/features/sessions/sessions-routing.module.ts',
    '<rootDir>/src/app/features/sessions/sessions.module.ts',
    '<rootDir>/src/app/guards/',
    '<rootDir>/src/app/interceptors/',
    '<rootDir>/src/app/interfaces/',
    '<rootDir>/src/assets/',
    '<rootDir>/src/favicon.ico',
    '<rootDir>/src/index.html',
    '<rootDir>/src/styles.scss',
    '<rootDir>/src/proxy.config.json'
  ],
  coverageThreshold: {
    global: {
      statements: 80,
      branches: 80,
      functions: 80,
      lines: 80
    },
  },
  moduleNameMapper: {
    '^src/(.*)$': '<rootDir>/src/$1',
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy'
  },
  roots: ['<rootDir>'],
  modulePaths: ['<rootDir>'],
  moduleDirectories: ['node_modules'],
  testMatch: [
    '<rootDir>/src/**/*.spec.ts'
  ],
  collectCoverageFrom: [
    'src/**/*.ts',
    '!src/**/*.spec.ts',
    '!src/**/*.d.ts',
    '!src/main.ts',
    '!src/polyfills.ts',
    '!src/environments/**',
    '!src/app/app-routing.module.ts',
    '!src/app/app.module.ts',
    '!src/app/features/auth/auth-routing.module.ts',
    '!src/app/features/auth/auth.module.ts',
    '!src/app/features/sessions/sessions-routing.module.ts',
    '!src/app/features/sessions/sessions.module.ts',
    '!src/app/guards/**',
    '!src/app/interceptors/**',
    '!src/app/interfaces/**'
  ],
  maxWorkers: '50%',
  testTimeout: 10000
};

