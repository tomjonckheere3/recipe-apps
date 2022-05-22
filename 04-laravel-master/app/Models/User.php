<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Illuminate\Database\Eloquent\Model;
use Tymon\JWTAuth\Contracts\JWTSubject;
use NotificationChannels\WebPush\HasPushSubscriptions;

class User extends Authenticatable implements JWTSubject
{
    use HasFactory, Notifiable;
    public $timestamps = false;
    use HasPushSubScriptions;

    protected $fillable = [
        "username",
        "password",
    ];

    protected $hidden = [
        "password",
        "created_at",
        "remember_token",
        "updated_at",
    ];

    protected $primaryKey = "userId";

    public function getJWTIdentifier() {
        return $this->getKey();
    }

    public function getJWTCustomClaims() {
        return [];
    }
}

