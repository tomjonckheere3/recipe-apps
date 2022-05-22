<?php

namespace App\Exceptions;

use Illuminate\Auth\Access\AuthorizationException;
use Illuminate\Foundation\Exceptions\Handler as ExceptionHandler;
use Illuminate\Validation\UnauthorizedException;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Throwable;

class Handler extends ExceptionHandler
{
    /**
     * A list of the exception types that are not reported.
     *
     * @var array
     */
    protected $dontReport = [
        //
    ];

    /**
     * A list of the inputs that are never flashed for validation exceptions.
     *
     * @var array
     */
    protected $dontFlash = [
        'current_password',
        'password',
        'password_confirmation',
    ];

    /**
     * Register the exception handling callbacks for the application.
     *
     * @return void
     */
    public function register()
    {
        $this->reportable(function (Throwable $e) {
            //
        });
    }

    public function render($request, Throwable $e)
    {
        if (!$request->wantsJson()) {
            return parent::render($request, $e);
        }
        if ($e instanceof AuthorizationException || $e instanceof UnauthorizedException) {
            return response(
                ["status" => "error", "errors" => ["messages" => ["401 Unauthorized or " . $e.getMessage() . " or expired. You need to relogin."]]],
                401
            );
        }
        if ($e instanceof NotFoundHttpException) {
            return response(
                ["status" => "error", "errors" => ["messages" => ["404 Not found"]]],
                404
            );
        }

        return parent::render($request, $e);
    }
}
